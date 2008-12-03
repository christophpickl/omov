package code {

import mx.messaging.ChannelSet;
import mx.messaging.channels.AMFChannel;
import mx.rpc.remoting.RemoteObject;

public dynamic class SomeService extends RemoteObject {
	
	private static const DESTINATION: String = "someService";
	private static const AMF_CHANNEL: String = "my-amf";
	
	private static const WEB_URL: String = "localhost";
	private static const WEB_PORT: String = "8080";
	private static const WEB_CONTEXT: String = "MyWeb";
	
	private static const AMF_FULL_URL: String = "http://"+WEB_URL+":"+WEB_PORT+"/"+WEB_CONTEXT+"/messagebroker/amf";
	
	
	public function SomeService() {
		super(DESTINATION);
		trace("Initializing remote object with proper channel sets.");
		
		const channelSet: ChannelSet = new ChannelSet();
		channelSet.addChannel(new AMFChannel(AMF_CHANNEL, AMF_FULL_URL));
		this.channelSet = channelSet;
	}
	
}
}