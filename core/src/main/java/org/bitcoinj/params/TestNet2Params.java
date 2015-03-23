/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;

import static com.google.common.base.Preconditions.checkState;

/**
 * Parameters for the old version 2 testnet. This is not useful to you - it exists only because some unit tests are
 * based on it.
 */
public class TestNet2Params extends NetworkParameters {
    public TestNet2Params() {
        super("04e6951f77356e7998fa968caedfc0aab07e3bb733a85a33930812d6aa23d8b518e3bda6033079934381527b99cda27ae2584e3a9e4f7c48b51bc7641c0cb9a1d5");
        id = ID_TESTNET;
        packetMagic = 0xfabfb5daL;
        port = 18392;
        addressHeader = 111;
        p2shHeader = 196;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(0x1e0fffffl);
        dumpedPrivateKeyHeader = 239;
        genesisBlock.setTime(1389138974l);
        genesisBlock.setDifficultyTarget(0x1e0fffffl);
        genesisBlock.setNonce(280528);
        spendableCoinbaseDepth = 100;
        subsidyDecreaseBlockCount = 210000;
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("5df7281a262d9a6251e65d12bdb78c6a69041c7d28e209df2a7719059de3088c"));
        dnsSeeds = null;
    }

    private static TestNet2Params instance;
    public static synchronized TestNet2Params get() {
        if (instance == null) {
            instance = new TestNet2Params();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return null;
    }
}
