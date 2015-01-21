package openFlowControll;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.Tenant;
import org.openstack4j.model.identity.User;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;

public class APIManager {
	public void test(){
		OSClient os = OSFactory.builder()
                		.endpoint("http://127.0.0.1:5000/v2.0")
                		.credentials("admin","sample")
                		.tenantName("admin")
                		.authenticate();
//		OSClient os1 = OSFactory.builderV3()
//						.endpoint("");
		OSClient os2 = OSFactory.builderV3()
						.endpoint("http://127.0.0.1:5000/v2.0")
						.credentials("admin","sample")
						.domainName("test")
						.authenticate();
		
		// Create a Tenant
		Tenant t = os.identity().tenants()
		                     .create(Builders.tenant()
		                     .name("ABC Corporation")
		                     .build());
		// Create a Server Model Object
		ServerCreate server = Builders.server().name("ubuntu 2").flavor("large").image("sdf").build();
		// Boot the Server
		os.compute().servers().boot(server);

		// Create a Snapshot
		os.compute().servers().createSnapshot("id", "name");
		
		// Create an Image
		Image image = os.images().create(Builders.image()
		                .name("Cirros 0.3.0 x64")
		                .isPublic(true)
		                .containerFormat(ContainerFormat.BARE)
		                .diskFormat(DiskFormat.QCOW2)
		                .build()
		                , Payloads.create(new File("cirros.img")));
		
		Port port = os.networking().port()
	              .create(Builders.port()
	              .name("port1")
	              .networkId("networkId")
	              .fixedIp("52.51.1.253", "subnetId")
	              .build());
		
		// Find all Users
		List<? extends User> users = os.identity().users().list();

		// List all Tenants
		List<? extends Tenant> tenants = os.identity().tenants().list();

		// Find all Compute Flavors
		List<? extends Flavor> flavors = os.compute().flavors().list();

		// Find all running Servers
		List<? extends Server> servers = os.compute().servers().list();

		// Suspend a Server
		os.compute().servers().action("serverId", Action.SUSPEND);

		// List all Networks
		List<? extends Network> networks = os.networking().network().list();

		// List all Subnets
		List<? extends Subnet> subnets = os.networking().subnet().list();

		// List all Routers
		List<? extends Router> routers = os.networking().router().list();

		// List all Images (Glance)
		List<? extends Image> images = os.images().list();

		// Download the Image Data
		InputStream is = os.images().getAsStream("imageId");		
	}
}
