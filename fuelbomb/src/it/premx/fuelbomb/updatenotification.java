/*
 * Copyright (C) 2015 Premx <premx at web.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package it.premx.fuelbomb;

/**
 *
 * @author Premx
 */
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class updatenotification {

    private fuelbomb plugin;
    private URL filesFeed;
    private String version;
    private String link;

    public updatenotification(fuelbomb plugin, String url) {
        this.plugin = plugin;

        try {
            this.filesFeed = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public boolean newupdate() {
        try {
            InputStream input = this.filesFeed.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

            Node latestFile = document.getElementsByTagName("item").item(0);

            NodeList children = latestFile.getChildNodes();

            this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
            this.link = children.item(3).getTextContent();

            if (!plugin.getDescription().getVersion().equals(this.version)) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getVersion() {
        return this.version;
    }

    public String getLink() {
        return this.link;
    }

}
