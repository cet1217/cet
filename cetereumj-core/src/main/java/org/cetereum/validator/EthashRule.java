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
package org.cetereum.validator;

import org.apache.commons.lang3.tuple.Pair;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.BlockHeader;
import org.cetereum.core.BlockSummary;
import org.cetereum.listener.CompositeCetereumListener;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.mine.CetashValidationHelper;
import org.cetereum.util.FastByteComparisons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.cetereum.validator.CetashRule.ChainType.main;
import static org.cetereum.validator.CetashRule.ChainType.reverse;
import static org.cetereum.validator.CetashRule.Mode.fake;
import static org.cetereum.validator.CetashRule.Mode.mixed;

/**
 * Runs block header validation against Cetash dataset.
 *
 * <p>
 *     Configurable to work in several modes:
 *     <ul>
 *         <li> fake - partial checks without verification against Cetash dataset
 *         <li> strict - full check for each block
 *         <li> mixed  - run full check for each block if main import flow during short sync,
 *                       run full check in random fashion (<code>1/{@link #MIX_DENOMINATOR}</code> blocks are checked)
 *                                during long sync, fast sync headers and blocks downloading
 *
 *
 * @author Mikhail Kalinin
 * @since 19.06.2018
 */
public class CetashRule extends BlockHeaderRule {

    private static final Logger logger = LoggerFactory.getLogger("blockchain");

    CetashValidationHelper cetashHelper;
    ProofOfWorkRule powRule = new ProofOfWorkRule();

    public enum Mode {
        strict,
        mixed,
        fake;

        static Mode parse(String name, Mode defaultMode) {
            for (Mode mode : values()) {
                if (mode.name().equals(name.toLowerCase()))
                    return mode;
            }
            return defaultMode;
        }
    }

    public enum ChainType {
        main,       /** main chain, cache updates are stick to best block events, requires listener */
        direct,     /** side chain, cache is triggered each validation attempt, no listener required */
        reverse;    /** side chain with reverted validation order */

        public boolean isSide() {
            return this == reverse || this == direct;
        }
    }

    private static final int MIX_DENOMINATOR = 5;
    private Mode mode = mixed;
    private ChainType chain = main;
    private boolean syncDone = false;
    private Random rnd = new Random();

    // two most common settings
    public static CetashRule createRegular(SystemProperties systemProperties, CompositeCetereumListener listener) {
        return new CetashRule(Mode.parse(systemProperties.getCetashMode(), mixed), main, listener);
    }

    public static CetashRule createReverse(SystemProperties systemProperties) {
        return new CetashRule(Mode.parse(systemProperties.getCetashMode(), mixed), reverse, null);
    }

    public CetashRule(Mode mode, ChainType chain, CompositeCetereumListener listener) {
        this.mode = mode;
        this.chain = chain;

        if (this.mode != fake) {
            this.cetashHelper = new CetashValidationHelper(
                    chain == reverse ? CetashValidationHelper.CacheOrder.reverse : CetashValidationHelper.CacheOrder.direct);

            if (this.chain == main && listener != null) {
                listener.addListener(new CetereumListenerAdapter() {
                    @Override
                    public void onSyncDone(SyncState state) {
                        CetashRule.this.syncDone = true;
                    }

                    @Override
                    public void onBlock(BlockSummary blockSummary, boolean best) {
                        if (best) cetashHelper.preCache(blockSummary.getBlock().getNumber());
                    }
                });
            }
        }
    }

    @Override
    public ValidationResult validate(BlockHeader header) {

        if (header.isGenesis())
            return Success;

        if (cetashHelper == null)
            return powRule.validate(header);

        // trigger cache for side chains before mixed mode condition
        if (chain.isSide())
            cetashHelper.preCache(header.getNumber());

        // mixed mode payload
        if (mode == mixed && !syncDone && rnd.nextInt(100) % MIX_DENOMINATOR > 0)
            return powRule.validate(header);

        try {
            Pair<byte[], byte[]> res = cetashHelper.cetashWorkFor(header, header.getNonce(), true);
            // no cache for the epoch? fallback into fake rule
            if (res == null) {
                return powRule.validate(header);
            }

            if (!FastByteComparisons.equal(res.getLeft(), header.getMixHash())) {
                return fault(String.format("#%d: mixHash doesn't match", header.getNumber()));
            }

            if (FastByteComparisons.compareTo(res.getRight(), 0, 32, header.getPowBoundary(), 0, 32) > 0) {
                return fault(String.format("#%d: proofValue > header.getPowBoundary()", header.getNumber()));
            }

            return Success;
        } catch (Exception e) {
            logger.error("Failed to verify cetash work for block {}", header.getShortDescr(), e);
            return fault("Failed to verify cetash work for block " + header.getShortDescr());
        }
    }
}
