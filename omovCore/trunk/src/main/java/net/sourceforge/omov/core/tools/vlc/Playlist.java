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

package net.sourceforge.omov.core.tools.vlc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * actually is capable of lot of more features, like:
 * - accessing folders
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Playlist {
	private final Map<String, Integer> idByUri = new HashMap<String, Integer>();
	private final Map<Integer, PlaylistNode> nodeById = new HashMap<Integer, PlaylistNode>();
	
	Playlist(PlaylistNode rootNode) {
		this.parseNode(rootNode);
	}
	
	private void parseNode(PlaylistNode node) {
		if(node.isLeaf == true) {
			this.idByUri.put(node.getUri(), new Integer(node.getId()));
		}
		this.nodeById.put(new Integer(node.getId()), node);
		
		for (PlaylistNode subNode : node.getSubNodes()) {
			this.parseNode(subNode);
		}
	}
	
	/**
	 * @param uri
	 * @return null if node with given uri is not existing in playlist.
	 */
	public Integer getIdByUri(String uri) {
		return this.idByUri.get(uri);
	}
	
	
	
	
	
	static class PlaylistNode {
		private final int id;
		private final List<PlaylistNode> subNodes = new LinkedList<PlaylistNode>();
		
		private final boolean isLeaf;
		private final String name;
		private final String uri;
		
		private PlaylistNode(int id, String name, String uri) {
			this.id = id;
			this.name = name;
			this.uri = uri;

			this.isLeaf = this.name == null;
			assert(this.isLeaf ? this.uri != null : this.name != null);
		}

		static PlaylistNode newNode(int id, String name) {
			return new PlaylistNode(id, name, null);
		}
		static PlaylistNode newLeaf(int id, String uri) {
			return new PlaylistNode(id, null, uri);
		}
		void addPlaylistNode(PlaylistNode node) {
			assert(this.isLeaf == false);
			this.subNodes.add(node);
		}
		public int getId() {
			return this.id;
		}
		public String getName() {
			if(this.isLeaf == true) throw new IllegalStateException("isLeaf == true");
			return this.name;
		}
		public String getUri() {
			if(this.isLeaf == false) throw new IllegalStateException("isLeaf == false");
			return this.uri;
		}
		public List<PlaylistNode> getSubNodes() {
			return this.subNodes;
		}
	}
	

	public enum VlcState {
		PLAYING,
		PAUSED,
		STOP,
		INVALID,
		UNKOWN;
	}
}
