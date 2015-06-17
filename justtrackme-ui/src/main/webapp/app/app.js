/* It's an initial version of Angular js app. Will be improved*/

var justtrackmeApp = angular.module('justtrackmeApp', ['ngRoute', 'errorHandler', 'leaflet-directive']);

// Http error handler
angular.module('errorHandler', []).factory('httpErrorInterceptor', ['$q', 'settings', function ($q, settings) {
    var myInterceptor = {
        'request': function (config) {
            config.msBeforeAjaxCall = new Date().getTime();
            return config;
        },
        'response': function (response) {
            return response;
        },
        'responseError': function (rejection) {
            if (rejection.status == 401) {
            	// TODO: Let's redirect to SSO
            	//console.log(settings.oauth2Url);
            }
            return $q.reject(rejection);
        }
    };
    
    return myInterceptor;
}]);

justtrackmeApp.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('httpErrorInterceptor');
}]);

// Configure routes
    justtrackmeApp.config(function($routeProvider) {
        $routeProvider
	        // route for default home page
	        .when('/home', {
	        	templateUrl : 'pages/home.html',
	        	controller  : 'homeController'
	        })
	        // route for map page
	        .when('/track', {
	        	templateUrl : 'pages/map.html',
	        	controller  : 'trackController'
	        })
	        .otherwise({redirectTo:'/track'});
    });