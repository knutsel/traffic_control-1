package Fixtures::Integration::Asn;

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
# 
#   http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.


# Do not edit! Generated code.
# See https://github.com/Comcast/traffic_control/wiki/The%20Kabletown%20example

use Moose;
extends 'DBIx::Class::EasyFixture';
use namespace::autoclean;

my %definition_for = (
'0' => { new => 'Asn', => using => { asn => '9939', cachegroup => '91', id => '1', last_updated => '2015-12-10 15:44:36', }, }, 
'1' => { new => 'Asn', => using => { asn => '9839', cachegroup => '91', id => '2', last_updated => '2015-12-10 15:44:36', }, }, 
'2' => { new => 'Asn', => using => { asn => '9933', cachegroup => '92', id => '3', last_updated => '2015-12-10 15:44:36', }, }, 
'3' => { new => 'Asn', => using => { asn => '9930', cachegroup => '93', id => '4', last_updated => '2015-12-10 15:44:36', }, }, 
'4' => { new => 'Asn', => using => { asn => '9931', cachegroup => '94', id => '5', last_updated => '2015-12-10 15:44:36', }, }, 
'5' => { new => 'Asn', => using => { asn => '9932', cachegroup => '95', id => '6', last_updated => '2015-12-10 15:44:36', }, }, 
'6' => { new => 'Asn', => using => { asn => '99', cachegroup => '96', id => '7', last_updated => '2015-12-10 15:44:36', }, }, 
'7' => { new => 'Asn', => using => { asn => '33', cachegroup => '96', id => '8', last_updated => '2015-12-10 15:44:36', }, }, 
); 

sub name {
		return "Asn";
}

sub get_definition { 
		my ( $self, $name ) = @_;
		return $definition_for{$name};
}

sub all_fixture_names {
		return keys %definition_for;
}

__PACKAGE__->meta->make_immutable;
1;
