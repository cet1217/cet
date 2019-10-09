package org.cetereum.manager;

import org.cetereum.core.Block;
import org.cetereum.core.Blockchain;
import org.cetereum.core.EventDispatchThread;
import org.cetereum.core.Genesis;
import org.cetereum.core.ImportResult;
import org.cetereum.db.DbFlushManager;
import org.cetereum.listener.CompositeCetereumListener;
import org.cetereum.validator.BlockHeaderRule;
import org.cetereum.validator.BlockHeaderValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.cetereum.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BlockLoaderTest.Config.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_MCETOD)
public class BlockLoaderTest {

    private static class Holder<T> {
        private T object;

        public Holder(T object) {
            this.object = object;
        }

        public T get() {
            return object;
        }

        public void set(T object) {
            this.object = object;
        }

        public boolean isEmpty() {
            return isNull(object);
        }
    }

    static class Config {

        private static final Genesis GENESIS_BLOCK = new Genesis(
                EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY,
                0, 0, 0, 0, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);

        @Bean
        public BlockHeaderValidator headerValidator() {
            BlockHeaderValidator validator = Mockito.mock(BlockHeaderValidator.class);
            when(validator.validate(any())).thenReturn(new BlockHeaderRule.ValidationResult(true, null));
            when(validator.validateAndLog(any(), any())).thenReturn(true);
            return validator;
        }

        @Bean
        public Blockchain blockchain(Holder<Block> lastBlockHolder) {
            Blockchain blockchain = Mockito.mock(Blockchain.class);
            when(blockchain.getBestBlock()).thenAnswer(invocation -> lastBlockHolder.get());
            when(blockchain.tryToConnect(any(Block.class))).thenAnswer(invocation -> {
                lastBlockHolder.set(invocation.getArgument(0));
                return ImportResult.IMPORTED_BEST;
            });
            return blockchain;
        }

        @Bean
        public Holder<Block> lastBlockHolder() {
            return new Holder<>(GENESIS_BLOCK);
        }

        @Bean
        public DbFlushManager dbFlushManager() {
            return Mockito.mock(DbFlushManager.class);
        }

        @Bean
        public CompositeCetereumListener cetereumListener() {
            return Mockito.mock(CompositeCetereumListener.class);
        }

        @Bean
        public EventDispatchThread dispatchThread() {
            return EventDispatchThread.getDefault();
        }

        @Bean
        public BlockLoader blockLoader(BlockHeaderValidator headerValidator, Blockchain blockchain, DbFlushManager dbFlushManager) {
            return new BlockLoader(headerValidator, blockchain, dbFlushManager);
        }
    }

    @Autowired
    private BlockLoader blockLoader;
    private List<Path> dumps;

    @Value("classpath:dmp")
    public void setDumps( Resource dmpDir) throws IOException {
        this.dumps = Arrays.stream(dmpDir.getFile().listFiles())
                .sorted()
                .map(dmp -> Paths.get(dmp.toURI()))
                .collect(toList());
    }

    @Test
    public void testRegularLoading() {
        Path[] paths = dumps.toArray(new Path[]{});
        boolean loaded = blockLoader.loadBlocks(paths);
        assertTrue(loaded);
    }

    @Test
    public void testInconsistentDumpsLoading() {
        List<Path> reversed = new ArrayList<>(dumps);
        Collections.reverse(reversed);
        Path[] paths = reversed.toArray(new Path[]{});
        boolean loaded = blockLoader.loadBlocks(paths);
        assertFalse(loaded);
    }

    @Test
    public void testNoDumpsLoading() {
        assertFalse(blockLoader.loadBlocks());
    }
}
