 justtrackmeApp.controller('trackController', [ '$scope', "$http", "$timeout", "$interval", "settings", function($scope, $http, $timeout, $interval, settings) {
        	$scope.devices = {};
        	$scope.selectedDevice = null;
        	$scope.requestTimeout = 5000;
        	$scope.pointAmount = 1;
        	$scope.trackCentered = false;
        	
            angular.extend($scope, {
            	center: {zoom: 14},
            	bounds: {},
                defaults: {
                    zoomAnimation: true,
                    markerZoomAnimation: true,
                    fadeAnimation: true
                },
                markers: []
            });
            
            $scope.changeDevice = function changeDevice(){
            	$scope.trackCentered = false;
            	$scope.loadTracks();
            }
            
            
            $scope.loadDevice = function loadTracks() {
                $http.get(settings.apiUrl + '/devices').success(function(data) {
                    $scope.devices = data.entities;
                });
            };
            
            $scope.loadTracks = function loadTracks() {
            	if($scope.selectedDevice){
                $http.get(settings.apiUrl + '/devices/'+ $scope.selectedDevice.uniqueId + '/positions?limit=' + $scope.pointAmount+ '&timestamp='+ new Date().getTime()).success(function(data) {
                    $scope.markers = [];
                    
                	angular.forEach(data.entities, function(location, key){
                		if(!$scope.trackCentered){
                			$scope.center = {lat: location.latitude, lng: location.longitude, zoom: 14};
                			$scope.trackCentered = true;
                		}
                		
                		$scope.markers[location.id] = {lat: location.latitude, lng: location.longitude};
                    });
                });
            		
            	}
            };
            
            $timeout(function(){
            	$scope.loadDevice();
            });
            
            $timeout(function(){
            	$scope.loadTracks();
            });
            
            $interval( function(){ $scope.loadTracks(); }, $scope.requestTimeout);
            
        } ]);