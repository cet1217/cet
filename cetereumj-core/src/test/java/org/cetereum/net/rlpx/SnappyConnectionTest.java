/*
 * Copyright (c) [2016] [ <ceter.camp> ]
 * This file is part of the cetereumJ library.
 *
 * The cetereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The cetereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the cetereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cetereum.net.rlpx;

import org.cetereum.config.NoAutoscan;
import org.cetereum.config.SystemProperties;
import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.net.cet.message.StatusMessage;
import org.cetereum.net.server.Channel;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Mikhail Kalinin
 * @since 02.11.2017
 */
@Ignore
public class SnappyConnectionTest {

    @Configuration
    @NoAutoscan
    public static class SysPropConfig1 {
        static SystemProperties props;
        @Bean
        public SystemProperties systemProperties() {
            return props;
        }
    }

    @Configuration
    @NoAutoscan
    public static class SysPropConfig2 {
        static SystemProperties props;
        @Bean
        public SystemProperties systemProperties() {
            return props;
        }
    }

    @Test
    public void test4To4() throws FileNotFoundException, InterruptedException {
        runScenario(4, 4);
    }

    @Test
    public void test4To5() throws FileNotFoundException, InterruptedException {
        runScenario(4, 5);
    }

    @Test
    public void test5To4() throws FileNotFoundException, InterruptedException {
        runScenario(5, 4);
    }

    @Test
    public void test5To5() throws FileNotFoundException, InterruptedException {
        runScenario(5, 5);
    }

    private void runScenario(int vOutbound, int vInbound) throws FileNotFoundException, InterruptedException {
        SysPropConfig1.props = new SystemProperties();
        SysPropConfig1.props.overrideParams(
                "peer.listen.port", "30334",
                "peer.privateKey", "ba43d10d069f0c41a8914849c1abeeac2a681b21ae9b60a6a2362c06e6eb1bc8",
                "database.dir", "test_db-1",
                "peer.p2p.version", String.valueOf(vInbound));
        SysPropConfig2.props = new SystemProperties();
        SysPropConfig2.props.overrideParams(
                "peer.listen.port", "30335",
                "peer.privateKey", "d3a4a240b107ab443d46187306d0b947ce3d6b6ed95aead8c4941afcebde43d2",
                "database.dir", "test_db-2",
                "peer.p2p.version", String.valueOf(vOutbound));

        Cetereum cetereum1 = CetereumFactory.createCetereum(SysPropConfig1.class);
        Cetereum cetereum2 = CetereumFactory.createCetereum(SysPropConfig2.class);

        final CountDownLatch semaphore = new CountDownLatch(2);

        cetereum1.addListener(new CetereumListenerAdapter() {
            @Override
            public void onCetStatusUpdated(Channel channel, StatusMessage statusMessage) {
                System.out.println("1: -> " + statusMessage);
                semaphore.countDown();
            }
        });
        cetereum2.addListener(new CetereumListenerAdapter() {
            @Override
            public void onCetStatusUpdated(Channel channel, StatusMessage statusMessage) {
                System.out.println("2: -> " + statusMessage);
                semaphore.countDown();
            }
        });

        cetereum2.connect(new Node("enode://a560c55a0a5b5d137c638eb6973812f431974e4398c6644fa0c19181954fab530bb2a1e2c4eec7cc855f6bab9193ea41d6cf0bf2b8b41ed6b8b9e09c072a1e5a" +
                "@localhost:30334"));

        semaphore.await(60, TimeUnit.SECONDS);

        cetereum1.close();
        cetereum2.close();

        if(semaphore.getCount() > 0) {
            throw new RuntimeException("One or both StatusMessage was not received: " + semaphore.getCount());
        }

        System.out.println("Passed.");
    }
}
