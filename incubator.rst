= Traffic Control Proposal =

== Abstract ==

Traffic Control allows you to build a large scale content delivery network using
open source. Built around Apache Traffic Server as the caching software, Traffic
Control implements all the core functions of a modern CDN.

== Proposal ==

The goal of this proposal is to bring the Traffic Control project into the
Apache Software Foundation.

== Background ==

Traffic Control allows you to build a large scale content delivery network using
open source. Built around Apache Traffic Server as the caching software, Traffic
Control implements all the core functions of a modern CDN:

* Traffic Router is a Java Tomcat application that routes clients to the closest
available cache on the CDN using both HTTP and DNS. By using consistent hashing
it sends requests for the same content to the same cache in a group of caches
working together in a location. It takes care of routing clients around hot
spots and problems in the CDN by using the information from Traffic Monitor with
regards to state of all the caches.

* Traffic Monitor is a Java Tomcat application that implements the CDN health
protocol. Every cache in the CDN is checked using HTTP for vital stats, and
based on these stats, caches are declared healthy or unhealthy. This information
is then used by Traffic Router to make its routing decisions.

* Traffic Ops is a Perl Mojolicious and jQuery UI application for management and
monitoring of all servers in the CDN. All server and content routing information
for the CDN is managed through Traffic Ops. It also exposes RESTful API
endpoints for consumption by tools and other applications.

* Traffic Stats is a Golang application that is used to acquire and store 
statistics about CDNs controlled by Traffic Control. Traffic Stats mines metrics
from Traffic Monitor’s JSON APIs and stores the data in InfluxDB.

* Traffic Analytics is a new component we are starting to build for log file
analysis, based on Apache Kafka, Heka and ElasticSearch.

Traffic Control was developed by Comcast Cable and released as open source under
the Apache 2.0 license in April of 2015. Traffic Control is deployed at Comcast
and other cable operators.

Traffic Control was presented at ApacheCon NA 2016, see http://bit.ly/1UwMzmR
for additional background information.

== Rationale ==

Even though the traffic on today's CDNs is strictly defined by open standards,
and there are many open source implementations of caches available, CDNs are
still proprietary. The current providers of CDN-as-a-product or
CDN-as-a-service all have their own proprietary implementation of the control
plane.  The CDN control plane of one vendor can't interoperate with the CDN
control plane of another, creating a classic vendor-lockin for CDN-as-a-product
customers. Traffic Control changes that. Emerging standards from IETF (CDNi
working group) and the Streaming Video Alliance Open Caching working group need
an open source reference implementation; Traffic Control will strive to be
that.

== Initial Goals ==

Initial goals of transitioning to ASF is to grow and diversify the community,
and to move to a more open and inclusive development model.

== Current Status ==

Traffic Control is functional and deployed at Comcast and other cable operators.
In the past 12 months 10 major releases have been made.

=== Meritocracy ====

Initial development was done at Comcast Cable. Since April 2015  it has been
open source, and a handful outside contributors have been added.

Our main goal during incubation is to try to create a more diverse group of
contributors and users.

=== Community ====

Traffic Control is being used by a number of cable companies and is being
evaluated by a number of vendors and ISPs. Two vendors have created products
based on Traffic Control and are active in the community.

=== Core Developers ====

Most of the core developers of Traffic Control are currently at Comcast. The
main goal of the incubation is to grow the developer and user group into a
community beyond Comcast and US cable.

=== Alignment ====

Traffic Control is closely aligned with Apache Traffic Server (ATS). The only
supported cache in a Traffic Control CDN at this time is ATS.  One of our
proposed mentors is a committers to ATS, and our proposed champion the ATS PMC
chair.

We don't want to become a sub-project of ATS though, because we believe we
should add other caching proxies as they are deemed to be a valuable addition to
the Traffic Control CDN.

== Known Risks ==

=== Orphaned products ==== 

Traffic Control is a new system that does not have wide adoption.

=== Inexperience with Open Source ====

One of the members of the team is an active Apache member and committer to ATS.
The rest of the team has been, in various ways, active in the ATS community in
recent years.

=== Homogenous Developers ====

Possibly our weakest area, and one of the drivers for us to want to become part
of Apache. Most people working on the project are at Comcast, or at least in the
cable industry.  Even though we are currently open sourced, as it stands, it is
not possible to add committers external to Comcast. We want to change that.  We
would add 3 external committers as a part of entering the incubator.

=== Reliance on Salaried Developers ====

Currently, Traffic Control relies solely on salaried employees.

=== Relationships with Other Apache Products ====

See also Alignment. ATS is a big part of a Traffic Control CDN, but we also use
Apache Tomcat, apache Maven, and a number of other apache libraries (see also
external dependencies).

=== A Excessive Fascination with the Apache Brand ====

We are more attracted to Apache as a philosophy than Apache as a brand. We
definitely see value in the brand, but we feel that adopting the "Apache Way"
is the most crucial factor for our long term viability.

== Documentation ==

Documentation is available at:

http://traffic-control-cdn.net/docs/latest/index.html

== Initial Source == 

The source code can be found here:

https://github.com/Comcast/traffic_control

== Source and Intellectual Property Submission Plan ==

The code is currently Apache 2.0 license, and was verified to have no
intellectual property or license issues before being being released to open
source by Comcast in 2015. Since then, extreme care has been taken to not add
any dependencies or code that would change that.

== External Dependencies ==

Note that all dependencies except two have been verified to have a Apache
compatible license. The two that are not compatible are MySQL (GPL), and we are
removing that dependency in version 2.0, and jdnssec (GPL), which we are
planning to replace in the future. A third, Heka is Mozilla Public License 2.0,
we are unsure if it is compatible (http://www.apache.org/legal/resolved.html
seems to say it is category B, so it is OK to have the binary dependency), but
the dependency is optional, and Heka will probably be replaced in the near
future.

* Golang
** github.com/gorilla/handlers
** github.com/dgrijalva/jwt-go/
** github.com/tebeka/selenium
** github.com/lib/pq
* Apache Kafka
* Heka (https://github.com/mozilla-services/heka - MPL)
* ElasticSearch
* Java
** org.apache.wicket
** org.slf4j
** log4j
** org.eclipse.jetty.aggregate
** org.apache.commons
** commons-codec
** com.ning.async-http-client
** org.hamcrest
** junit
** org.powermock
** org.springframework
** javax.servlet
** com.fasterxml.jackson.core
** org.apache.tomcat
** org.json
** dnsjava
** jdnssec # GPL, needs to be removed
** com.google.guava
** org.apache.wicket
** com.googlecode.java-ipv6
** com.maxmind.geoip2  # maxmind commercial - optional
** com.google.http-client
** org.apache.httpcomponents
** org.eclipse.jetty.aggregate
** com.fasterxml.jackson.core
** com.quova.bff # neustar commercial - optional
* MySQL # Note: being replaced in version 2.0 with Postgres
* Postgres
* postgrest (https://github.com/begriffs/postgrest)
* Riak 
* InfluxDB
* Grafana
* goose (https://bitbucket.org/liamstask/goose/)
* Perl packages
**  CPAN package 'Algorithm::C3'
**  CPAN package 'B::Hooks::EndOfScope'
**  CPAN package 'CPAN::Meta'
**  CPAN package 'CPAN::Meta::Check'
**  CPAN package 'CPAN::Meta::Requirements'
**  CPAN package 'CPAN::Meta::YAML'
**  CPAN package 'Carp'
**  CPAN package 'Carp::Clan'
**  CPAN package 'Class::Accessor'
**  CPAN package 'Class::Accessor::Chained::Fast'
**  CPAN package 'Class::Accessor::Grouped'
**  CPAN package 'Class::C3'
**  CPAN package 'Class::C3::Componentised'
**  CPAN package 'Class::Inspector'
**  CPAN package 'Class::Load'
**  CPAN package 'Class::Load::XS'
**  CPAN package 'Class::Method::Modifiers'
**  CPAN package 'Class::Unload'
**  CPAN package 'Clone'
**  CPAN package 'Compress::Raw::Bzip2'
**  CPAN package 'Compress::Raw::Zlib'
**  CPAN package 'Compress::Zlib'
**  CPAN package 'Config::Any'
**  CPAN package 'Config::General'
**  CPAN package 'Config::Properties'
**  CPAN package 'Context::Preserve'
**  CPAN package 'Data::GUID'
**  CPAN package 'DBD::SQLite'
**  CPAN packagerequires 'Time::HiRes' # required by DBD::mysql
**  CPAN package 'DBD::mysql', '==4.029'
**  CPAN package 'DBI'
**  CPAN package 'DBIx::Class'
**  CPAN package 'DBIx::Class::Core'
**  CPAN package 'DBIx::Class::EasyFixture'
**  CPAN package 'DBIx::Class::IntrospectableM2M'
**  CPAN package 'DBIx::Class::Schema'
**  CPAN package 'DBIx::Class::Schema::Loader'
**  CPAN package 'DBIx::Class::IntrospectableM2M'
**  CPAN package 'Data::Compare'
**  CPAN package 'Data::Dumper'
**  CPAN package 'Data::Dumper::Concise'
**  CPAN package 'Data::OptList'
**  CPAN package 'Data::Page'
**  CPAN package 'Date::Manip'
**  CPAN package 'DateTime::Format::ISO8601'
**  CPAN package 'Devel::GlobalDestruction'
**  CPAN package 'Devel::StackTrace'
**  CPAN package 'Devel::Symdump'
**  CPAN package 'Digest::MD5'
**  CPAN package 'Digest::SHA'
**  CPAN package 'Digest::SHA1'
**  CPAN package 'Digest::base'
**  CPAN package 'DirHandle'
**  CPAN package 'Dist::CheckConflicts'
**  CPAN package 'DynaLoader'
**  CPAN package 'Email::Valid'
**  CPAN package 'Encode'
**  CPAN package 'Encode::Locale'
**  CPAN package 'Env'
**  CPAN package 'Eval::Closure'
**  CPAN package 'Exporter'
**  CPAN package 'ExtUtils::CBuilder'
**  CPAN package 'ExtUtils::Install'
**  CPAN package 'ExtUtils::MakeMaker'
**  CPAN package 'ExtUtils::Manifest'
**  CPAN package 'ExtUtils::ParseXS'
**  CPAN package 'File::Basename'
**  CPAN package 'File::Copy::Recursive'
**  CPAN package 'File::Find'
**  CPAN package 'File::Find::Rule'
**  CPAN package 'File::Listing'
**  CPAN package 'File::Path'
**  CPAN package 'File::Spec'
**  CPAN package 'File::Stat'
**  CPAN package 'File::Spec::Functions'
**  CPAN package 'File::Temp'
**  CPAN package 'Getopt::Long'
**  CPAN package 'Getopt::Std'
**  CPAN package 'HTML::Entities'
**  CPAN package 'HTML::Parser'
**  CPAN package 'HTML::Tagset'
**  CPAN package 'HTTP::Cookies'
**  CPAN package 'HTTP::Daemon'
**  CPAN package 'HTTP::Date'
**  CPAN package 'HTTP::Headers::Util'
**  CPAN package 'HTTP::Negotiate'
**  CPAN package 'Hash::Merge'
**  CPAN package 'IO::Compress::Bzip2'
**  CPAN package 'IO::Compress::Gzip'
**  CPAN package 'IO::Uncompress::Unzip'
**  CPAN package 'IO::File'
**  CPAN package 'IO::HTML'
**  CPAN package 'IO::Handle'
**  CPAN package 'IO::Socket::SSL'
**  CPAN package 'IO::Socket::Timeout'
**  CPAN package 'IO::String'
**  CPAN package 'IPC::Cmd'
**  CPAN package 'Import::Into'
**  CPAN package 'JSON'
**  CPAN package 'JSON::PP'
**  CPAN package 'JSON::XS'
**  CPAN package 'LWP'
**  CPAN package 'LWP::MediaTypes'
**  CPAN package 'Lingua::EN::FindNumber'
**  CPAN package 'Lingua::EN::Inflect'
**  CPAN package 'Lingua::EN::Inflect::Number'
**  CPAN package 'Lingua::EN::Inflect::Phrase'
**  CPAN package 'Lingua::EN::Number::IsOrdinal'
**  CPAN package 'Lingua::EN::Tagger'
**  CPAN package 'Lingua::EN::Words2Nums'
**  CPAN package 'Lingua::GL::Stemmer'
**  CPAN package 'Lingua::Stem'
**  CPAN package 'Lingua::Stem::Fr'
**  CPAN package 'Lingua::Stem::It'
**  CPAN package 'Lingua::Stem::Ru'
**  CPAN package 'Lingua::Stem::Snowball::Da'
**  CPAN package 'Lingua::Stem::Snowball::No'
**  CPAN package 'Lingua::Stem::Snowball::Se'
**  CPAN package 'List::Compare'
**  CPAN package 'List::MoreUtils'
**  CPAN package 'Locale::Maketext::Simple'
**  CPAN package 'Log::Log4perl'
**  CPAN package 'MIME::Base64'
**  CPAN package 'Math::Round'
**  CPAN package 'MRO::Compat'
**  CPAN package 'Memoize'
**  CPAN package 'Memoize::ExpireLRU'
**  CPAN package 'Mixin::Linewise::Readers'
**  CPAN package 'Modern::Perl','==1.20150127'
**  CPAN package 'Module::Build'
**  CPAN package 'Module::Build::ModuleInfo'
**  CPAN package 'Module::CoreList'
**  CPAN package 'Module::Find'
**  CPAN package 'Module::Implementation'
**  CPAN package 'Module::Load'
**  CPAN package 'Module::Load::Conditional'
**  CPAN package 'Module::Metadata'
**  CPAN package 'Module::Pluggable'
**  CPAN package 'Module::Runtime'
**  CPAN package 'Module::ScanDeps'
**  CPAN package 'Mojo::Base'
**  CPAN package 'Mojo::JSON'
**  CPAN package 'Mojo::Log'
**  CPAN package 'Mojo::Upload'
**  CPAN package 'Mojo::UserAgent'
**  CPAN package 'Mojolicious', '==5.24'
**  CPAN package 'Mojolicious::Lite'
**  CPAN package 'Mojolicious::Plugin::AccessLog', '==0.004'
**  CPAN package 'Mojolicious::Plugin::Authentication', '==1.26'
**  CPAN package 'Mojolicious::Plugin::FormFields', '==0.04'
**  CPAN package 'Mojolicious::Plugin::Mail', '==1.3'
**  CPAN package 'MojoX::Log::Log4perl', '==0.10'
**  CPAN package 'Moo'
**  CPAN package 'Moose'
**  CPAN package 'NetPacket::IPv6'
**  CPAN package 'Net::FTP'
**  CPAN package 'Net::HTTP'
**  CPAN package 'Net::LDAP'
**  CPAN package 'Net::Pcap'
**  CPAN package 'Net::PcapUtils'
**  CPAN package 'NetAddr::IP'
**  CPAN package 'NetPacket'
**  CPAN package 'Number::Compare'
**  CPAN package 'POSIX'
**  CPAN package 'Package::DeprecationManager'
**  CPAN package 'Package::Stash'
**  CPAN package 'Package::Stash::XS'
**  CPAN package 'Params::Check'
**  CPAN package 'Params::Util'
**  CPAN package 'Parse::CPAN::Meta'
**  CPAN package 'Path::Class'
**  CPAN package 'Perl::OSType'
**  CPAN package 'Perl::Tidy','==20150815'
**  CPAN package 'PerlIO::utf8_strict'
**  CPAN package 'PerlIO::via::Timeout'
**  CPAN package 'Pod::Coverage::CountParents'
**  CPAN package 'Pod::Coverage::TrustPod'
**  CPAN package 'Pod::Escapes'
**  CPAN package 'Pod::Eventual::Simple'
**  CPAN package 'Pod::Find'
**  CPAN package 'Pod::Man'
**  CPAN package 'Pod::Simple'
**  CPAN package 'Pod::Usage'
**  CPAN package 'Role::Tiny'
**  CPAN package 'SQL::Abstract'
**  CPAN package 'Scalar::Util'
**  CPAN package 'Scope::Guard'
**  CPAN package 'Socket'
**  CPAN package 'Storable'
**  CPAN package 'String::CamelCase'
**  CPAN package 'String::ToIdentifier::EN'
**  CPAN package 'Sub::Exporter'
**  CPAN package 'Sub::Exporter::Progressive'
**  CPAN package 'Sub::Identify'
**  CPAN package 'Sub::Install'
**  CPAN package 'Sub::Name'
**  CPAN package 'Sub::Uplevel'
**  CPAN package 'Sys::Syslog'
**  CPAN package 'TAP::Formatter::Jenkins'
**  CPAN package 'Task::Weaken'
**  CPAN package 'Term::ReadPassword'
**  CPAN package 'Test'
**  CPAN package 'Test::Builder::Tester'
**  CPAN package 'Test::CPAN::Meta'
**  CPAN package 'Test::Deep'
**  CPAN package 'Test::Exception'
**  CPAN package 'Test::Fatal'
**  CPAN package 'Test::Harness'
**  CPAN package 'Test::Inter'
**  CPAN package 'Test::Mojo'
**  CPAN package 'Test::MockModule'
**  CPAN package 'Test::MockObject'
**  CPAN package 'Test::More'
**  CPAN package 'Test::NoWarnings'
**  CPAN package 'Test::Pod'
**  CPAN package 'Test::SharedFork'
**  CPAN package 'Test::TCP'
**  CPAN package 'Test::Tester'
**  CPAN package 'Test::Warn'
**  CPAN package 'Test::Requires'
**  CPAN package 'Text::Abbrev'
**  CPAN package 'Text::Balanced'
**  CPAN package 'Text::German'
**  CPAN package 'Text::Glob'
**  CPAN package 'Text::ParseWords'
**  CPAN package 'Text::Unidecode'
**  CPAN package 'Text::Wrap'
**  CPAN package 'Time::HiRes'
**  CPAN package 'Time::Local'
**  CPAN package 'Time::Out'
**  CPAN package 'Time::Seconds'
**  CPAN package 'Try::Tiny'
**  CPAN package 'URI'
**  CPAN package 'WWW::Curl::Easy'
**  CPAN package 'WWW::RobotRules'
**  CPAN package 'XSLoader'
**  CPAN package 'ExtUtils::Config'     # for Net::Riak
**  CPAN package 'Module::Build::Tiny'  # for Net::Riak
**  CPAN package 'Net::Riak'
**  CPAN package 'Crypt::OpenSSL::RSA'
**  CPAN package 'Crypt::OpenSSL::Bignum'
**  CPAN package 'Crypt::OpenSSL::Random'
**  CPAN package 'Net::DNS::SEC::Private'
**  CPAN package 'LWP::Protocol::https'
**  CPAN package 'Net::CIDR'
**  CPAN package 'Data::Validate::IP'

== Cryptography == 

There is no cryptographic code in Traffic Control. We leverage OpenSSL for
all our cryptography needs.

== Required Resources ==

We would like to utilize GitHub as much as possible, but some continuous
integration resources would be needed.

Mailing lists, see below.

=== Mailing lists ===

We currently use "google groups" to communicate, but we would like to move that
to ASF maintained mailing lists.

Current groups / mailing lists:
* https://groups.google.com/forum/#!forum/traffic_control
* https://groups.google.com/forum/#!forum/traffic_control-announce
* https://groups.google.com/forum/#!forum/traffic_control-discuss

Proposed ASF maintained lists:
* private@traffic-control.incubator.apache.org (moderated subscriptions)
* dev@traffic-control.incubator.apache.org
* commits@traffic-control.incubator.apache.org
* notifications@traffic-control.incubator.apache.org

=== Subversion Directory ====

We do not use SVN for source code revision control.

=== Git Repository ====

Our development model is based in GitHub and we would prefer to use the
Git-Dual setup that ATS is currently trialing.

=== Issue Tracking ====

GitHub issues.

=== Other Resources ====

We have automated tests and continuous integration configurations we would like
to move away from Comcast.

== Initial Committers ==

* Dan Kirkwood (dangogh at gmail.com)
* David Neuman (david.neuman64 at gmail.com)
* Dewayne Richardson (dewrich at gmail.com)
* Eric Friedrich (efriedri at cisco.com)
* Hank Beatty (Hank.Beatty at cox.com>)
* Jackie Heitzer (jackieheitzer at gmail.com)
* Jan van Doorn (jvd at knutsel.com)
* Jeff Elsloo (jeff.elsloo at gmail.com)
* Jeremy Mitchell (mitchell852 at gmail.com)
* Mark Torluemke (mark at torluemke.net)
* Steve Malenfant (steve.malenfant at cox.com)

== Affiliations ==

* Comcast Cable: Dan Kirkwood, David Neuman, Dewayne Richardson, Jackie Heitzer,
Jan van Doorn, Jeff Elsloo, Jeremy Mitchell, Mark Torluemke

* Cox Communications: Hank Beatty, Steve Malenfant

* Cisco: Eric Friedrich

== Sponsors ==

=== Champion ====

* Leif Hedstrom (zwoop at apache.org)

=== Nominated Mentors ====

* Phil Sorber (sorber at apache.org)
* Eric Covener (covener at apache.org)
* Daniel Gruno (humbedooh at apache.org)
* J. Aaron Farr (farra at apache.org)

=== Sponsoring Entity ====

We request the Apache Incubator to sponsor this project.
