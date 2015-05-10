package UI::Redis;
#
# Copyright 2015 Comcast Cable Communications Management, LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#
#

use UI::Utils;
use TrafficOps;
use Mojo::Base 'Mojolicious::Controller';
use JSON;
use Redis;
use Data::Dumper;
use Time::HiRes qw(gettimeofday tv_interval);
use Math::Round qw(nearest);
use Utils::Helper;
use Carp qw(cluck confess);

sub stats {
	my $self     = shift;                       # /redis/#match/#start/#end/#interval
	my $match    = $self->param('match');
	my $start    = $self->param('start');       # start time in secs since 1970, or "now" to get latest sample
	my $end      = $self->param('end');         # end time in secs since 1970, or "now" to get latest sample
	my $interval = $self->param('interval');    # the interval between the samples. 10 is minimum, has to be a multiple of 10

	# demo jvd hack 
	# my $j = $self->get_stats( $match, $start, $end, $interval );
	my $file = undef;
	if ( $match eq "cdn_number_1:all:all:all:kbps" ) {
		$file = "/Users/jvd/Downloads/ott60.json";
	}
	elsif ( $match eq "cdn_number_2:all:all:all:kbps" ) {
		$file = "/Users/jvd/Downloads/t660.json";
	}
	elsif ( $match eq "cdn_number_1:all:all:all:ats.proxy.process.http.current_client_connections" ) {
		$file = "/Users/jvd/Downloads/ottt60.json";
	}
	elsif ( $match eq "cdn_number_2:all:all:all:ats.proxy.process.http.current_client_connections" ) {
		$file = "/Users/jvd/Downloads/t6t60.json";

	}
	local $/;
	open( F1, '<' . $file ) || $self->app->log->error( ">>> " . $! );
	my $json_text = <F1>;
	my $j         = decode_json($json_text);
	my $timebase  = time;
	print $timebase . " /... \n";
	my $last = 0;
	my $nseries = 0;
	foreach my $series ( @{ $j->{series} } ) {
		$series->{timeBase} = $timebase;
		# print "Setting time base to $timebase for $match \n";
		my $i = 0;
		foreach my $sample ( @{ $series->{samples} } ) {
			$timebase += 240;
			if ( $match =~ /cdn_number_2/ ) {
				$series->{samples}->[$i] = $series->{samples}->[$i] * 9.9;
			}
			$i++;
		}
		$nseries++;
		$last = $i -1;
	}
	my $multi = 100;
	if ($match =~ /kbps$/) {
		$multi = 1000000;
	}
	$j->{series}->[$nseries-1]->{samples}->[$last] = $j->{series}->[$nseries-1]->{samples}->[$last] + rand($multi);
	# $j->{series}->[$#j->{series}]->{samples}->[$#j->] = $j->{series}->[0]->{samples} + rand(10);
	# push(@{$j->{series}->[0]->{samples}}, $j->{series}->[0]->{samples} + int(rand(10)));
	# / demo jvd hack 

	$self->render( json => $j );
}

sub info {
	my $self      = shift;
	my $shortname = $self->param('shortname');

	my $server = $self->db->resultset('Server')->search( { host_name => $shortname } )->single();
	my $ip     = $server->ip_address;
	my $port   = $server->tcp_port;

	my $redis = redis_connect();

	my $data = undef;
	foreach my $sub (qw/Server Clients Memory Persistence Stats Replication CPU Keyspace/) {
		my $subdata = $redis->info($sub);
		$data->{$sub} = $subdata;
	}

	my $i = 0;
	my @slowlist = $redis->slowlog( "get", 1024 );
	foreach my $slowlog (@slowlist) {
		push( @{ $data->{slowlog} }, $slowlog );
		$i++;
	}
	$data->{slowlen} = $i;

	$redis->quit();
	$self->render( json => $data );
}

1;
