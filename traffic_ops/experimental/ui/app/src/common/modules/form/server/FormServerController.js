/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var FormServerController = function(server, $scope, formUtils, stringUtils, locationUtils, cacheGroupService, cdnService, locationService, profileService, statusService, typeService) {

    var getLocations = function() {
        locationService.getLocations()
            .then(function(result) {
                $scope.locations = result;
            });
    };

    var getCacheGroups = function() {
        cacheGroupService.getCacheGroups()
            .then(function(result) {
                $scope.cacheGroups = result;
            });
    };

    var getTypes = function() {
        typeService.getTypes('server')
            .then(function(result) {
                $scope.types = result;
            });
    };

    var getCDNs = function() {
        cdnService.getCDNs()
            .then(function(result) {
                $scope.cdns = result;
            });
    };

    var getStatuses = function() {
        statusService.getStatuses()
            .then(function(result) {
                $scope.statuses = result;
            });
    };

    var getProfiles = function() {
        profileService.getProfiles()
            .then(function(result) {
                $scope.profiles = result;
            });
    };

    // supposedly matches IPv4 and IPv6 formats. but actually need one that matches each. todo.
    var ipRegex = new RegExp(/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$|^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/);

    $scope.server = server;

    $scope.props = [
        { name: 'hostName', type: 'text', required: true, maxLength: 45, pattern: new RegExp(/^\S*$/), invalidMsg: 'No Spaces' },
        { name: 'domainName', type: 'text', required: true, maxLength: 45, pattern: new RegExp(/^\S*$/), invalidMsg: 'No Spaces' },
        { name: 'tcpPort', type: 'text', required: false, maxLength: 10, pattern: new RegExp(/^\d+$/), invalidMsg: 'Number' },
        { name: 'xmppId', type: 'text', required: false, maxLength: 256 },
        { name: 'xmppPasswd', type: 'text', required: false, maxLength: 45 },
        { name: 'interfaceName', type: 'text', required: true, maxLength: 45 },
        { name: 'ipAddress', type: 'text', required: true, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'ipNetmask', type: 'text', required: true, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'ipGateway', type: 'text', required: true, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'ip6Address', type: 'text', required: false, maxLength: 50, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'ip6Gateway', type: 'text', required: false, maxLength: 50, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'interfaceMtu', type: 'text', required: true, maxLength: 11, pattern: new RegExp(/(^1500$|^9000$)/), invalidMsg: '1500 or 9000' },
        { name: 'rack', type: 'text', required: false, maxLength: 64 },
        { name: 'offlineReason', type: 'text', required: false, maxLength: 256 },
        { name: 'mgmtIpAddress', type: 'text', required: false, maxLength: 50, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'mgmtIpNetmask', type: 'text', required: false, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'mgmtIpGateway', type: 'text', required: false, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'iloIpAddress', type: 'text', required: false, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'iloIpNetmask', type: 'text', required: false, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'iloIpGateway', type: 'text', required: false, maxLength: 45, pattern: ipRegex, invalidMsg: 'Invalid' },
        { name: 'iloUsername', type: 'text', required: false, maxLength: 45, pattern: new RegExp(/^\S*$/), invalidMsg: 'No Spaces' },
        { name: 'iloPassword', type: 'text', required: false, maxLength: 45 },
        { name: 'routerHostName', type: 'text', required: false, maxLength: 256, pattern: new RegExp(/^\S*$/), invalidMsg: 'No Spaces' },
        { name: 'routerPortName', type: 'text', required: false, maxLength: 256 },
        { name: 'httpsPort', type: 'text', required: false, maxLength: 10, pattern: new RegExp(/^\d+$/), invalidMsg: 'Number' },
    ];

    $scope.labelize = stringUtils.labelize;

    $scope.falseTrue = [
        { value: false, label: 'false' },
        { value: true, label: 'true' }
    ];

    $scope.navigateToPath = locationUtils.navigateToPath;

    $scope.hasError = formUtils.hasError;

    $scope.hasPropertyError = formUtils.hasPropertyError;

    var init = function () {
        getLocations();
        getCacheGroups();
        getTypes();
        getCDNs();
        getStatuses();
        getProfiles();
    };
    init();

};

FormServerController.$inject = ['server', '$scope', 'formUtils', 'stringUtils', 'locationUtils', 'cacheGroupService', 'cdnService', 'locationService', 'profileService', 'statusService', 'typeService'];
module.exports = FormServerController;
