= Traffic Control Proposal =

== Abstract ==
Traffic Control allows you to build a large scale content delivery network using 
open source. Built around Apache Traffic Server as the caching software, Traffic 
Control implements all the core functions of a modern CDN.

== Proposal ==
The goal of this proposal is to bring the Traffic Control software into the
Apache Software Foundation.

== Background ==
Traffic Control allows you to build a large scale content delivery network
using open source. Built around Apache Traffic Server as the caching software,
Traffic Control implements all the core functions of a modern CDN:

* Traffic Router is a Java Tomcat application that routes clients to the closest
available cache on the CDN using both HTTP and DNS. By using consistent
hashing it sends requests for the same content to the same cache in a group of
caches working together in a location. It takes care of routing clients around
hot spots and problems in the CDN by using the information from Traffic
Monitor with regards to state of all the caches.

* Traffic Monitor is a Java Tomcat application that implements the CDN health
protocol. Every cache in the CDN is checked using HTTP for vital stats, and
based on these stats, caches are declared healthy or unhealthy. This
information is then used by Traffic Router to make its routing decisions.

* Traffic Ops is a Mojolicious perl and jQuery UI application for management and
monitoring of all servers in the CDN. All server and content routing
information for the CDN is managed through Traffic Ops. It also exposes
RESTful API endpoints for consumption by tools and other applications.

* Traffic Stats is a Golang application that is used to acquire and store statistics 
about CDNs controlled by Traffic Control. Traffic Stats mines metrics from Traffic Monitor’s 
JSON APIs and stores the data in InfluxDb.

Traffic Control was developed by Comcast Cable and released as open source
under the Apache 2.0 license in April of 2015. Traffic Control is deployed at
Comcast and other cable operators. (JvD note: add blurb about scale?)

== Rationale ==
Even though the traffic on today's CDNs is strictly defined by open standards,
and there are many open source implementations of caches available, CDNs are
still proprietary. The current providers of CDN-as-a-product or
CDN-as-a-service all have their own proprietary implementation of the control
plane.  The CDN control plane of one vendor can't "work with" the CDN control
plane of another, creating a classic vendor-lockin for CDN-as-a-product
customers. Traffic Control changes that.  (JvD note: need to change / expand)

== Initial Goals ==
Traffic Control is functional and deployed at Comcast and other cable
operators. In the past 6 months 5 releases have been made.


== Current Status ==
=== Meritocracy ====
Initial development was done at Comcast Cable. Since April 2015  it has been
open source, and a few outside contributors have been added.

Our main goal during incubation is to try to create a more diverse group of
contributors and users.

(JvD note: need to change / expand)

=== Community ====
Traffic Control is being used by a number of cable companies and is being
evaluated by a number of vendors and ISPs.

=== Core Developers ====
All of the Core developers of Traffic Control are currently at Comcast. The main goal of
the incubation is to grow the developer and user group into a community beyond
Comcast and US cable.

=== Alignment ====
Traffic Control is closely aligned with Apache Traffic Server (ATS). The only
supported cache in a Traffic Control CDN at this time is ATS.  Two of our
initial committers are committers to ATS, and our proposed champion the ATS PMC chair.

We don't want to become a sub-project of ATS though, because we believe we
should add other caching proxies as they are deemed to be a valuable addition
to the Traffic Control CDN.


== Known Risks ==

=== Orphaned products ====
Traffic Control is a new system that does not have wide adoption.

=== Inexperience with Open Source ====
One of the members of the team is an active Apache member and committer to
ATS. The rest of the team has been, in various ways, an active member of the
ATS community. (JvD note: expand? How cable is changing?)


=== Homogenous Developers ====
Possibly our weakest area, and one of the drivers for us to want to become
part of Apache. Most people working on the project are at Comcast, or at least
in the cable industry.  Even though we are currently open sourced, as it stands, 
it is not possible to add committers external to Comcast. We want to change that. 
We would add 3 external committers as a part of entering the incubator.

=== Reliance on Salaried Developers ====
Currently, Traffic Control relies solely on salaried employees.

=== Relationships with Other Apache Products ====
See also Alignment. ATS is a big part of a Traffic Control CDN, but we also
use Apache Tomcat, apache Maven, and a number of other apache libraries (? JvD
Note).

=== A Excessive Fascination with the Apache Brand ====


== Documentation ==

Documentation is available at:

http://traffic-control-cdn.net/docs/latest/index.html

== Initial Source ==
The source code can be found here:

https://github.com/Comcast/traffic_control

== Source and Intellectual Property Submission Plan ==

== External Dependencies ==

== Cryptography ==

None.

== Required Resources ==
=== Mailing lists ===

* private@traffic-control.incubator.apache.org (moderated subscriptions)
* dev@traffic-control.incubator.apache.org
* commits@traffic-control.incubator.apache.org
* notifications@traffic-control.incubator.apache.org

=== Subversion Directory ====
 -

=== Git Repository ====
We will move the source to git-wip-us.apache.org once accepted into the
incubator.

=== Issue Tracking ====
 JIRA ?

=== Other Resources ====


== Initial Committers ==
Dan Kirkwood (dangogh at gmail.com)
David Neuman (david.neuman64 at gmail.com)
Dewayne Richardson (dewrich at gmail.com)
Hank Beatty ()
Jackie Heitzer (jackieheitzer at gmail.com)
Jan van Doorn (jvd at knutsel.com)
Jeff Elsloo (jeff.elsloo at gmail.com)
Jeremy Mitchell (mitchell852 at gmail.com)
John Rushford ? ()
Leif Hedstrom (zwoop at apache.org)
Mark Torluemke (mark at torluemke.net)
Phil Sorber (sorber at apache.org)
Steve Malenfant ()
Eric Friedrich ()

== Affiliations ==
Comcast Cable: Dan Kirkwood, David Neuman, Dewayne Richardson, Jackie Heitzer,
    Jan van Doorn, Jeff Elsloo, Jeremy Mitchell, Mark Torluemke, Phil Sorber

Cox Communications: Hank Beatty, Steve Malenfant

Cisco: Eric Friedrich

== Sponsors ==

=== Champion ====
* Leif Hedstrom

=== Nominated Mentors ====
* Phil Sorber
* Eric Covener
* Daniel Gruno
* … Riot Guy … 


=== Sponsoring Entity ====
* The Incubator PMC.

