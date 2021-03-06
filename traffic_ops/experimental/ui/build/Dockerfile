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

FROM centos:7
MAINTAINER Dan Kirkwood

RUN	yum -y install \
		epel-release \
		git \
		rpm-build && \
	yum -y clean all

ENV GITREPO https://github.com/apache/incubator-trafficcontrol.git
ENV BRANCH master

WORKDIR /repo

# traffic_ops_v2 specific
RUN	yum -y install \
		gcc \
		libffi-devel \
		make \
		nodejs \
		npm \
		ruby-devel \
		rubygems

RUN	gem install compass
RUN	npm -g install bower grunt-cli

# bower will not run as root by default
RUN	echo '{ "allow_root": true }' > /root/.bowerrc

###

VOLUME ./artifacts:/artifacts

ADD run-build.sh .

CMD ./run-build.sh traffic_ops_v2 2>&1 | tee /artifacts/build-traffic_ops_v2.log

# vi:syntax=Dockerfile
