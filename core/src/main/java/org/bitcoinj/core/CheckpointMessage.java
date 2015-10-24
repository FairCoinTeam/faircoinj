/*
 * Copyright 2011 Google Inc.
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

package org.bitcoinj.core;


/**
 * Checkpoints are signed messages that are broadcast on the peer-to-peer network if they match a hard-coded signing key.
 * They are used to secure the network by protecting against unwanted chain forks.
 *     
 * Before doing anything with a checkpoint, you should check {@link CheckpointMessage#isSignatureValid()}.
 */
public class CheckpointMessage extends Message {
    private static final long serialVersionUID = 8906949630881176030L;
    
    private byte[] content;
    private byte[] signature;

    // See the getters for documentation of what each field means.
    private long version = 1;
    private Sha256Hash checkpointHash;
    
    public CheckpointMessage(NetworkParameters params, byte[] payloadBytes) throws ProtocolException {
        super(params, payloadBytes, 0);
    }

    @Override
    public String toString() {
        return "sync-checkpoint: " + getCheckpointHash();
    }

    @Override
    void parse() throws ProtocolException {
        // Checkoints are formatted in two levels. The top level contains two byte arrays: a signature, and a serialized
        // data structure containing the actual checkpoint data.
        int startPos = cursor;
        content = readByteArray();
        signature = readByteArray();
        // Now we need to parse out the contents of the embedded structure. Rewind back to the start of the message.
        cursor = startPos;
        readVarInt();  // Skip the length field on the content array.
        // We're inside the embedded structure.
        version = readUint32();
        
        checkpointHash = readHash();

        length = cursor - offset;
    }

    /**
     * Returns true if the digital signature attached to the message verifies. Don't do anything with the alert if it
     * doesn't verify, because that would allow arbitrary attackers to spam your users.
     */
    public boolean isSignatureValid() {
        return ECKey.verify(Utils.doubleDigest(content), signature, params.getCheckpointSigningKey());
    }

    @Override
    protected void parseLite() throws ProtocolException {
        // Do nothing, lazy parsing isn't useful for checkpoints.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Field accessors.

    public Sha256Hash getCheckpointHash() {
        return checkpointHash;
    }

    public void setCheckpointHash(Sha256Hash checkpointHash) {
        this.checkpointHash = checkpointHash;
    }

    public long getVersion() {
        return version;
    }
}
