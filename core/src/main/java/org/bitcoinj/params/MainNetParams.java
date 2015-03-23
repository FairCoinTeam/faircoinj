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

import org.bitcoinj.core.Block;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.faircoin.Groestl512;

import static com.google.common.base.Preconditions.checkState;
import static org.bitcoinj.core.Utils.doubleDigest;
import static org.bitcoinj.core.Utils.singleDigest;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends NetworkParameters {
    public MainNetParams() {
        super("04b81f9a8d519834e3e35d46c6a3526ec317408a256f54f54cf54ffe8514ce702b705129a53a63f90af8796bb4a2633ffde7522b900f4ce253db92b7a2c799cfab");
        
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(0x1e0fffffL);
        dumpedPrivateKeyHeader = 223;
        addressHeader = 95;
        p2shHeader = 36;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        port = 46392;
        packetMagic = 0xe4e8e9e5l;
        genesisBlock.setDifficultyTarget(0x1e0fffffl);
        genesisBlock.setTime(1389138974l);
        genesisBlock.setNonce(102078l);
        id = ID_MAINNET;
        subsidyDecreaseBlockCount = 210000;
        spendableCoinbaseDepth = 100;
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("f1ae188b0c08e296e45980f9913f6ad2304ff02d5293538275bacdbcb05ef275"),
                genesisHash);

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
        checkpoints.put( 40000, new Sha256Hash("5346de84305836f881fb15a884088286bf23d3361e8e1e9d0b58916c46817801"));
        checkpoints.put( 80000, new Sha256Hash("f6f8b3d1334057117fddc385dabfbc3bce3826e170985c804af914db99a1f7f6"));
        checkpoints.put( 94614, new Sha256Hash("31d3eef28d9c6c1a15d7b12571d93ab563cbd661f06a2d47d2fb0323e6fca1aa"));

        dnsSeeds = new String[] {
                "seed1.fair-coin.org","seed2.fair-coin.org",
        };
    }

	public static Sha256Hash calculateBlockPoWHash(Block b) {
        byte[] blockHeader = b.cloneAsHeader().bitcoinSerialize();
        
        try {
			switch (b.getAlgo()) {
			    default:
			    case ALGO_SHA256D:
			    	return new Sha256Hash(Utils.reverseBytes(doubleDigest(blockHeader)));
			    case ALGO_GROESTL:
			    	return calculateGroestlHash(blockHeader);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return b.getHash();
		}
    }

	private static final Groestl512 groestl = new Groestl512();
	
	public static Sha256Hash calculateGroestlHash(byte[] bytes) throws Exception {
		groestl.reset();
		byte[] test = groestl.digest(bytes);
    	
        return new Sha256Hash(Utils.reverseBytes(singleDigest(test, 0, test.length)));
    }
	
    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
