/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
