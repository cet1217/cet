package org.cetereum.mine;

import org.junit.Test;

import static org.cetereum.mine.CetashValidationHelper.CacheOrder.direct;
import static org.cetereum.mine.CetashValidationHelper.CacheOrder.reverse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Mikhail Kalinin
 * @since 11.07.2018
 */
public class CetashValidationHelperTest {

    static class CetashAlgoMock extends org.cetereum.mine.CetashAlgo {
        @Override
        public int[] makeCache(long cacheSize, byte[] seed) {
            return new int[0];
        }
    }

    @Test // sequential direct caching
    public void testRegularFlow() {
        CetashValidationHelper cetash = new CetashValidationHelper(direct);
        cetash.cetashAlgo = new CetashAlgoMock();

        // init on block 193, 0 and 1st epochs are cached
        cetash.preCache(193);

        assertNotNull(cetash.getCachedFor(193));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);

        cetash = new CetashValidationHelper(direct);
        cetash.cetashAlgo = new CetashAlgoMock();

        // genesis
        cetash.preCache(0);

        assertNotNull(cetash.getCachedFor(0));
        assertEquals(cetash.caches.size(), 1);
        assertNull(cetash.getCachedFor(30_000));

        // block 100, nothing has changed
        cetash.preCache(100);
        assertNotNull(cetash.getCachedFor(100));
        assertEquals(cetash.caches.size(), 1);

        // block 193, next epoch must be added
        cetash.preCache(193);
        assertNotNull(cetash.getCachedFor(193));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);

        // block  30_192, nothing has changed
        cetash.preCache(30_192);
        assertNotNull(cetash.getCachedFor(30_192));
        assertNotNull(cetash.getCachedFor(192));
        assertEquals(cetash.caches.size(), 2);

        // block  30_193, two epochs are kept: 1st and 2nd
        cetash.preCache(30_193);
        assertNotNull(cetash.getCachedFor(30_193));
        assertNotNull(cetash.getCachedFor(60_000));
        assertNull(cetash.getCachedFor(193));
        assertEquals(cetash.caches.size(), 2);
    }

    @Test // sequential direct caching with gap between 0 and K block, K and N block
    public void testRegularFlowWithGap() {
        CetashValidationHelper cetash = new CetashValidationHelper(direct);
        cetash.cetashAlgo = new CetashAlgoMock();

        // genesis
        cetash.preCache(0);

        assertNotNull(cetash.getCachedFor(0));
        assertEquals(cetash.caches.size(), 1);
        assertNull(cetash.getCachedFor(30_000));

        // block 100_000, cache must have been reset
        cetash.preCache(100_000);
        assertNotNull(cetash.getCachedFor(100_000));
        assertNotNull(cetash.getCachedFor(120_000));
        assertNull(cetash.getCachedFor(0));
        assertEquals(cetash.caches.size(), 2);

        // block 120_193, caches shifted by one epoch
        cetash.preCache(120_193);
        assertNotNull(cetash.getCachedFor(120_000));
        assertNotNull(cetash.getCachedFor(150_000));
        assertNull(cetash.getCachedFor(100_000));
        assertEquals(cetash.caches.size(), 2);

        // block 300_000, caches have been reset once again
        cetash.preCache(300_000);
        assertNotNull(cetash.getCachedFor(300_000));
        assertNotNull(cetash.getCachedFor(299_000));
        assertNull(cetash.getCachedFor(120_000));
        assertNull(cetash.getCachedFor(150_000));
        assertEquals(cetash.caches.size(), 2);
    }

    @Test // sequential reverse flow, like a flow that is used in reverse header downloading
    public void testReverseFlow() {
        CetashValidationHelper cetash = new CetashValidationHelper(reverse);
        cetash.cetashAlgo = new CetashAlgoMock();

        // init on 15_000 block, 0 and 1st epochs are cached
        cetash.preCache(15_000);
        assertNotNull(cetash.getCachedFor(15_000));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);

        cetash = new CetashValidationHelper(reverse);
        cetash.cetashAlgo = new CetashAlgoMock();

        // init on 14_999 block, only 0 epoch is cached
        cetash.preCache(14_999);
        assertNotNull(cetash.getCachedFor(14_999));
        assertNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 1);

        cetash = new CetashValidationHelper(reverse);
        cetash.cetashAlgo = new CetashAlgoMock();

        // init on 100_000 block, 2nd and 3rd epochs are cached
        cetash.preCache(100_000);
        assertNotNull(cetash.getCachedFor(100_000));
        assertNotNull(cetash.getCachedFor(80_000));
        assertNull(cetash.getCachedFor(120_000));
        assertEquals(cetash.caches.size(), 2);

        // block 75_000, nothing has changed
        cetash.preCache(75_000);
        assertNotNull(cetash.getCachedFor(100_000));
        assertNotNull(cetash.getCachedFor(75_000));
        assertNull(cetash.getCachedFor(120_000));
        assertNull(cetash.getCachedFor(59_000));
        assertEquals(cetash.caches.size(), 2);

        // block 74_999, caches are shifted by 1 epoch toward 0 epoch
        cetash.preCache(74_999);
        assertNotNull(cetash.getCachedFor(74_999));
        assertNotNull(cetash.getCachedFor(59_000));
        assertNull(cetash.getCachedFor(100_000));
        assertEquals(cetash.caches.size(), 2);

        // block 44_999, caches are shifted by 1 epoch toward 0 epoch
        cetash.preCache(44_999);
        assertNotNull(cetash.getCachedFor(44_999));
        assertNotNull(cetash.getCachedFor(19_000));
        assertNull(cetash.getCachedFor(80_000));
        assertEquals(cetash.caches.size(), 2);

        // block 14_999, caches are shifted by 1 epoch toward 0 epoch
        cetash.preCache(14_999);
        assertNotNull(cetash.getCachedFor(14_999));
        assertNotNull(cetash.getCachedFor(0));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);

        // block 1, nothing has changed
        cetash.preCache(1);
        assertNotNull(cetash.getCachedFor(1));
        assertNotNull(cetash.getCachedFor(0));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);

        // block 0, nothing has changed
        cetash.preCache(0);
        assertNotNull(cetash.getCachedFor(0));
        assertNotNull(cetash.getCachedFor(30_000));
        assertEquals(cetash.caches.size(), 2);
    }

    @Test // sequential reverse flow with gap
    public void testReverseFlowWithGap() {
        CetashValidationHelper cetash = new CetashValidationHelper(reverse);
        cetash.cetashAlgo = new CetashAlgoMock();

        // init on 300_000 block
        cetash.preCache(300_000);
        assertNotNull(cetash.getCachedFor(300_000));
        assertNotNull(cetash.getCachedFor(275_000));
        assertNull(cetash.getCachedFor(330_000));
        assertEquals(cetash.caches.size(), 2);

        // jump to 100_000 block, 2nd and 3rd epochs are cached
        cetash.preCache(100_000);
        assertNotNull(cetash.getCachedFor(100_000));
        assertNotNull(cetash.getCachedFor(80_000));
        assertNull(cetash.getCachedFor(120_000));
        assertEquals(cetash.caches.size(), 2);

        // block 74_999, caches are shifted by 1 epoch toward 0 epoch
        cetash.preCache(74_999);
        assertNotNull(cetash.getCachedFor(74_999));
        assertNotNull(cetash.getCachedFor(59_000));
        assertNull(cetash.getCachedFor(100_000));
        assertEquals(cetash.caches.size(), 2);

        // jump to 14_999, caches are shifted by 1 epoch toward 0 epoch
        cetash.preCache(14_999);
        assertNotNull(cetash.getCachedFor(14_999));
        assertNotNull(cetash.getCachedFor(0));
        assertEquals(cetash.caches.size(), 1);
    }
}
