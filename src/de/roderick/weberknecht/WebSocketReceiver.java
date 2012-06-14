/*
 *  Copyright (C) 2011 Roderick Baier
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */

package de.roderick.weberknecht;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;


public class WebSocketReceiver
        extends Thread
{
    private static final Log LOG = LogFactory.getLog(WebSocketReceiver.class);

    private InputStream input = null;
    private WebSocketConnection websocket = null;
    private WebSocketEventHandler eventHandler = null;

    private static final long BYTE_BUFFER_SIZE = 128 * 1024;
    private static final long BYTE_BUFFER_WARN_THRESHOLD = Math.round(BYTE_BUFFER_SIZE * 1.5);
    private static final long BYTE_BUFFER_FAILURE_THRESHOLD = Math.round(BYTE_BUFFER_SIZE * 3);
    private static final long CONSECUTIVE_EMPTY_WARN_THRESHOLD = 10;
    private static final long CONSECUTIVE_EMPTY_FAILURE_THRESHOLD = CONSECUTIVE_EMPTY_WARN_THRESHOLD * 2;

    private volatile boolean stop = false;

    
    public WebSocketReceiver(InputStream input, WebSocketConnection websocket)
    {
        this.input = input;
        this.websocket = websocket;
        this.eventHandler = websocket.getEventHandler();
    }


    public void run()
    {
        // 128k byte buffer
        TByteList byteList = new TByteArrayList((int)BYTE_BUFFER_SIZE);
        boolean warned = false;
        long consecutiveEmptyMessages = 0;

        while (!stop) {
            try {
                if(byteList.size() > BYTE_BUFFER_WARN_THRESHOLD && !warned) {
                    LOG.warn("Byte buffer size is above warn threshold");
                    warned = true;
                } else if(byteList.size() > BYTE_BUFFER_FAILURE_THRESHOLD) {
                    LOG.fatal("Byte buffer size is above failure threshold - this indicates data loss!");
                    byteList.clear();
                    handleError();
                }

                int b = input.read();
                if (b == 0x00 || b == 0xFF) {
                    if(!byteList.isEmpty()) {
                        eventHandler.onMessage(new WebSocketMessage(byteList.toArray()));
                        consecutiveEmptyMessages = 0;
                    } else {
                        consecutiveEmptyMessages++;

                        if(consecutiveEmptyMessages == CONSECUTIVE_EMPTY_WARN_THRESHOLD) {
                            LOG.warn(String.format("Received %d consecutive empty messages",
                                    consecutiveEmptyMessages));
                        } else if(consecutiveEmptyMessages == CONSECUTIVE_EMPTY_FAILURE_THRESHOLD) {
                            LOG.fatal(String.format("Received %d consecutive empty messages, aborting stream!",
                                    consecutiveEmptyMessages));
                            handleError();
                        }
                    }

                    byteList.clear();
                    warned = false;
                } else if (b == -1) {
                    byteList.clear();
                    warned = false;
                    handleError();
                } else {
                    byteList.add((byte)b);
                }
            } catch (IOException ioe) {
                LOG.warn("IO exception during WebSocket stream", ioe);

                handleError();
                byteList.clear();
            }
        }
    }
    
    
    public void stopit()
    {
        stop = true;
    }
    
    
    public boolean isRunning()
    {
        return !stop;
    }
    
    
    private void handleError()
    {
        stopit();
        websocket.handleReceiverError();
    }
}
