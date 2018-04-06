'use strict';

var rest = require('rest');
var defaultRequest = require('rest/interceptor/defaultRequest');
var mime = require('rest/interceptor/mime');
var errorCode = require('rest/interceptor/errorCode');
var baseRegistry = require('rest/mime/registry');

var registry = baseRegistry.child();

module.exports = rest
		.wrap(mime, { registry: registry })
		.wrap(errorCode)
		.wrap(defaultRequest, { headers: { 'Accept': 'application/json', 'Content-Type': 'application/json'}});