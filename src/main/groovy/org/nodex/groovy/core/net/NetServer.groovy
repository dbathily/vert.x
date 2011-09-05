package org.nodex.groovy.core.net

import org.nodex.core.EventHandler

/*
* Copyright 2002-2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use
* this file except in compliance with the License. You may obtain a copy of the
* License at http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed
* under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
* CONDITIONS OF ANY KIND, either express or implied. See the License for the
* specific language governing permissions and limitations under the License.
*/

public class NetServer {

  def jServer

  NetServer() {
    jServer = new org.nodex.core.net.NetServer()
  }

  def connectHandler(hndlr) {
    // Wrap the Groovy closure in a an anonymous class so the java core can call it
    def gHandler = new org.nodex.core.EventHandler() {
      void onEvent(jSocket) {
        hndlr.call(new NetSocket(jSocket))
      }
    }
    jServer.connectHandler(gHandler)
  }

  def listen(int port) {
    jServer.listen(port)
  }

  class NetSocket {

    def jSocket

    NetSocket(jSocket) {
      this.jSocket = jSocket
    }

    def dataHandler(hndlr) {
      // Wrap the Groovy closure in a an anonymous class so the java core can call it
      def gHandler = new org.nodex.core.EventHandler() {
        void onEvent(jBuffer) {
          hndlr.call(jBuffer)
        }
      }
      jSocket.dataHandler(gHandler)
    }

    def write(buff) {
      jSocket.write(buff)
    }
  }
}
