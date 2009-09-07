/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.rss;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class FeedParserTest
{

  /**
   * Test of getInstance method, of class FeedParser.
   *
   * @throws IOException
   * @throws MalformedURLException
   */
  @Test
  public void test() throws MalformedURLException, IOException
  {
    FeedParser parser = FeedParser.getInstance("rss2");

    assertNotNull(parser);

    Channel channel = new Channel("Test", new URL("http://www.google.de"),
                                  " RSS - Test");

    channel.setCopyright("COPY");
    channel.setPubDate(new Date());
    channel.setImage(new Image(new URL("http://www.google.de/bild.jpg")));
    channel.getItems().add(new Item("Eintrag 1",
                                    new URL("http://www.google.de?e=1"),
                                    "Der erste Eintrag"));
    channel.getItems().add(new Item("Eintrag 2",
                                    new URL("http://www.google.de?e=2"),
                                    "Der zweite Eintrag"));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    parser.store(channel, baos);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    Channel eChannel = parser.load(bais);

    assertEquals(channel.getTitle(), eChannel.getTitle());
    assertEquals(channel.getLink(), eChannel.getLink());
    assertEquals(channel.getDescription(), eChannel.getDescription());
    assertEquals(channel.getCopyright(), eChannel.getCopyright());
    assertEquals(channel.getPubDate().toString(),
                 eChannel.getPubDate().toString());
    assertEquals(channel.getImage().getUrl(), eChannel.getImage().getUrl());
    assertEquals(channel.getItems().get(0).getTitle(),
                 eChannel.getItems().get(0).getTitle());
    assertEquals(channel.getItems().get(0).getLink(),
                 eChannel.getItems().get(0).getLink());
    assertEquals(channel.getItems().get(0).getDescription(),
                 eChannel.getItems().get(0).getDescription());
    assertEquals(channel.getItems().get(1).getTitle(),
                 eChannel.getItems().get(1).getTitle());
    assertEquals(channel.getItems().get(1).getLink(),
                 eChannel.getItems().get(1).getLink());
    assertEquals(channel.getItems().get(1).getDescription(),
                 eChannel.getItems().get(1).getDescription());
  }
}