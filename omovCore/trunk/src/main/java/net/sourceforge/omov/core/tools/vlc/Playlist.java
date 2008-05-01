package net.sourceforge.omov.core.tools.vlc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * actually is capable of lot of more features, like:
 * - accessing folders
 */
public class Playlist {
	private final Map<String, Integer> idByUri = new HashMap<String, Integer>();
	private final Map<Integer, PlaylistNode> nodeById = new HashMap<Integer, PlaylistNode>();
	
	Playlist(PlaylistNode rootNode) {
		this.parseNode(rootNode);
	}
	
	private void parseNode(PlaylistNode node) {
		if(node.isLeaf == true) {
			this.idByUri.put(node.getUri(), node.getId());
		}
		this.nodeById.put(node.getId(), node);
		
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
