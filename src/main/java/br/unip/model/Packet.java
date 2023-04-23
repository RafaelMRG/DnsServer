package br.unip.model;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.HashMap;

public class Packet implements Serializable {
    private String dnsIp;
    private String dnsUrl;

    public byte[] toByteArray(){
        return SerializationUtils.serialize(this);
    }

    static public Packet toPacket(byte[] data){
        return SerializationUtils.deserialize(data);
    }

    public String getDnsIp() {
        return dnsIp;
    }

    public Packet setDnsIp(String dnsIp) {
        this.dnsIp = dnsIp;
        return this;
    }

    public String getDnsUrl() {
        return dnsUrl;
    }

    public Packet setDnsUrl(HashMap<String, String> dnsDict) {
        String url = dnsDict.get(this.dnsIp);

        if (this.dnsIp.equals("END")){
            this.dnsUrl = "END";
            return this;
        }

        if (url == null){
            this.dnsUrl = "A URL para este ip ainda não foi registrada";
            return this;
        }

        this.dnsUrl = url;
        return this;
    }

    public boolean isAppCloseRequest(){
        return this.dnsUrl.equals("END") || this.dnsIp.equals("END");
    }
}
