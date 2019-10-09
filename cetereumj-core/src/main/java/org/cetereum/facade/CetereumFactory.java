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
package org.cetereum.facade;

import org.cetereum.config.DefaultConfig;
import org.cetereum.config.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author Roman Mandeleil
 * @since 13.11.2014
 */
@Component
public class CetereumFactory {

    private static final Logger logger = LoggerFactory.getLogger("general");

    public static Cetereum createCetereum() {
        return createCetereum((Class) null);
    }

    public static Cetereum createCetereum(Class userSpringConfig) {
        return userSpringConfig == null ? createCetereum(new Class[] {DefaultConfig.class}) :
                createCetereum(DefaultConfig.class, userSpringConfig);
    }

    /**
     * @deprecated The config parameter is not used anymore. The configuration is passed
     * via 'systemProperties' bean either from the DefaultConfig or from supplied userSpringConfig
     * @param config  Not used
     * @param userSpringConfig   User Spring configuration class
     * @return  Fully initialized Cetereum instance
     */
    public static Cetereum createCetereum(SystemProperties config, Class userSpringConfig) {

        return userSpringConfig == null ? createCetereum(new Class[] {DefaultConfig.class}) :
                createCetereum(DefaultConfig.class, userSpringConfig);
    }

    public static Cetereum createCetereum(Class ... springConfigs) {
        logger.info("Starting CetereumJ...");
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(springConfigs);
        context.registerShutdownHook();
        return context.getBean(Cetereum.class);
    }
}
