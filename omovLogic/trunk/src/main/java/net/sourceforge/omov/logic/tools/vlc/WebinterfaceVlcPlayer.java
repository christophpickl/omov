/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.logic.tools.vlc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.logic.tools.vlc.Playlist.PlaylistNode;
import net.sourceforge.omov.logic.tools.vlc.Playlist.VlcState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
abstract class WebinterfaceVlcPlayer implements IVlcPlayer {

    private static final Log LOG = LogFactory.getLog(WebinterfaceVlcPlayer.class);

	private static final String HOST = "http://localhost:8080/"; // TODO outsource VLC port configuration into PreferencesSource
	
	
	private String fetchLocalPage(String page) throws IOException {
		final String fullUrl = HOST + page;
		LOG.debug("Fetching local page: " + fullUrl);
		URL url = new URL(fullUrl);
		URLConnection connection = url.openConnection(); // no proxy!
//		HttpURLConnection httpConn = (HttpURLConnection) connection;
		
		StringBuilder sb = new StringBuilder();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while( (line = input.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} finally {
			if(input != null) try { input.close(); } catch(IOException e) { LOG.error("Could not close reader!", e); }
		}
		return sb.toString();
	}
	
	/**
	 * method not yet in use
	 */
	public VlcState getVlcStatus() throws IOException {
		final String page = fetchLocalPage("requests/status.xml");
		final int beginIndex = page.indexOf("<state>") + "<state>".length();
		final int endIndex = page.indexOf("</state>");
		final String status = page.substring(beginIndex, endIndex);
		
		if(status == null) {
			return VlcState.UNKOWN;
		} else if(status.equals("playing")) {
			return VlcState.PLAYING;
		} else if(status.equals("paused")) {
			return VlcState.PAUSED;
		} else if(status.equals("stop")) {
			return VlcState.STOP;
		} else {
			LOG.error("unhandled vlc state '"+status+"'!");
			return VlcState.INVALID;
		}
	}
	
	
	/**
	 * 
	 * <node id="" name="Undefined">
	 *   <node id="" name="General">
	 *     <leaf id="" uri=""/>
	 *   </node>
	 * </node>
	 * 
	 */
	public Playlist getPlaylist() throws BusinessException {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document document = builder.parse(new InputSource(new StringReader(page)));
		
		try {
			final String page = fetchLocalPage("requests/playlist.xml");
			final DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new StringReader(page)));
			final Document doc = parser.getDocument();
			final PlaylistNode pRootNode = parsePlaylistXml(null, doc.getFirstChild());
			return new Playlist(pRootNode);
		} catch (IOException e) {
			throw new BusinessException("Could not get playlist!", e);
		} catch (SAXException e) {
			throw new BusinessException("Could not get playlist!", e);
		}
	}
	
	private PlaylistNode parsePlaylistXml(final PlaylistNode pNode, Node xNode) {
		NamedNodeMap attributes = xNode.getAttributes();
		
		final int id = Integer.parseInt(attributes.getNamedItem("id").getTextContent());
		
		final PlaylistNode pSubNode;
		if(xNode.getNodeName().equals("node")) {
			final String name = attributes.getNamedItem("name").getTextContent();
			
			if(pNode == null) { // set initial root node
				// FIXME is the first parameter actually necessary??? ... pNode = PlaylistNode.newNode(id, name);
			}
			pSubNode = PlaylistNode.newNode(id, name);
			
			NodeList xSubNodes = xNode.getChildNodes();
			for (int i = 0; i < xSubNodes.getLength(); i++) {
				Node xSubNode = xSubNodes.item(i);
				pSubNode.addPlaylistNode(parsePlaylistXml(pSubNode, xSubNode));
			}
			
		} else { // xNode.name != "node"
			assert(xNode.getNodeName().equals("leaf"));
			assert(pNode != null);
			
			final String uri = attributes.getNamedItem("uri").getTextContent();
			pSubNode = PlaylistNode.newLeaf(id, uri);
		}
		return pSubNode;
	}
	
	public boolean playFile(final File file) throws BusinessException {
		final String uri = file.getAbsolutePath(); // MANTIS [22] for windows, use some uri conform string, e.g. "file://x/y/z.mpg"
		LOG.debug("Playing file in VLC (uri=" + uri + ")");
		Integer id = getPlaylist().getIdByUri(uri);
		if(id != null) {
			try {
				playById(id.intValue());
			} catch (IOException e) {
				throw new BusinessException("Could not play file '"+file.getAbsolutePath()+"'!", e);
			}
			return true;
		}
		LOG.warn("Could not playlist entry by uri '"+uri+"'!");
		return false;
	}
	
	private void playById(int id) throws IOException {
		LOG.info("Playing VLC file with id '"+id+"'.");
		fetchLocalPage("requests/status.xml?command=pl_play&id=" + id);		
	}
	
}
