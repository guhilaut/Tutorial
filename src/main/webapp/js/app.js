'use strict';

var showHideLoader = function(type, id) {
    if (type == 'hide') {
      $('body' + ((id) ? " " + id : "")).removeClass('loading').loader('hide');
      $('body').css('overflow', 'auto');
    } else if (type == 'show') {
      $('body').css('overflow', 'hidden');
      $('body' + ((id) ? " " + id : "")).addClass('loading').loader('show', {
        overlay: true
      }, 3000);

    }
  }
  // window.onpopstate = function(e) {
  //   window.history.forward(1);
  // }
var intervelId = null
  // Initialize base modules
var mercuryServices = angular.module('mercuryApp.HttpService', []);
var mercuryDirectives = angular.module('mercuryApp.directives', []);
var fameControllers = angular.module('mercuryApp.controllers', []);
var mercuryFilters = angular.module('mercuryApp.filters', []);
var LogListTable;
var mercuryApp = angular.module('mercuryApp', [
  'mercuryApp.controllers',
  'ui.bootstrap',
  'ui.router',
  'ngCookies',
  'FBAngular',
  'ngTouch',
  "ngAnimate",
  'ngSanitize',
  "angular-growl",
  "xeditable"
]);
mercuryApp.factory('httpInterceptor', function httpInterceptor($q, $window, $location) {
  return function(promise) {
    return promise.then(function(response) {
      showHideLoader('hide');
      return response;
    }, function(response) {
      showHideLoader('hide');
      if (response.status === 403) {
        $location.url('/app/env');
      }
      return $q.reject(response);
    });
  };
});
mercuryApp.factory('myHttpInterceptor', function($q, $window, $location) {
  return {

    // optional method
    'responseError': function(response) {
      //console.log(response);

      if (response.status === 403) {
        setTimeout(function() {
          showHideLoader('hide');
        })

        $location.url('/app/notfound');
      } else if (response.status === 500) {
        setTimeout(function() {
          showHideLoader('hide');
        })

        $location.url('/app/errorfound');
      }
      return $q.reject(response);
    }
  };
});
mercuryApp.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$locationProvider', "growlProvider", "$cookiesProvider", function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, growlProvider, $cookiesProvider) {
  $httpProvider.interceptors.push('myHttpInterceptor');
  growlProvider.globalTimeToLive(6000);
  growlProvider.globalEnableHtml(true);

  $stateProvider
    .state('app', {
      url: "/app",
      abstract: true,
      templateUrl: "partials/appView.html",
      controller: 'AppCtrl',
      resolve: {
        serverlist: function($http, $state, $cookieStore, $rootScope) {
          if ($cookieStore.get("login") && ($state.current.name || $state.current.abstract) && $state.current.name != "app.env" && $state.current.name != "app.notfound" && $state.current.name != "app.errorfound") {
            showHideLoader('show');
            return $http.post(appurl + '/home/loadServers/' + $cookieStore.get("login"), {
              "name": $cookieStore.get("username"),
              "password": $cookieStore.get("password")
            }).success(function(response) {
              if (response.flag) {
                $rootScope.serverlist = response.serverInfoList;
              }
            }).finally(function() {
              showHideLoader('hide');
            })
          } else {
            return null;
          }

        },
      }
    })
    .state('app.env', {
      url: "/env",
      views: {
        'content': {
          templateUrl: "partials/env.html",
          controller: "envCtrl"
        }
      }
    })
    .state('app.servers', {
      url: "/envdetail/:activeTab/:evnid",
      views: {
        'content': {
          templateUrl: "partials/servers.html",
          controller: "serversCtrl"
        }
      }
    })
    .state('app.server', {
      url: "/:activeTab/:evnid/serverdashboard/:ip",
      views: {
        'content': {
          templateUrl: "partials/report.html",
          controller: "reportCtrl"
        }
      }
    })

  .state('app.quartz', {
      url: "/:activeTab/:evnid/:ip/quartz/:schedulerGroup",
      views: {
        'content': {
          templateUrl: "partials/quartz-scheduler-details.html",
          controller: "quartzSchedulerCtrl"
        }
      }
    })
    .state('app.dsdetail', {
      url: "/:activeTab/:evnid/:ip/dsdetail/:dsGroup",
      views: {
        'content': {
          templateUrl: "partials/ds-detail.html",
          controller: "dsdetailCtrl"
        }
      }
    })
    .state('app.bundledetails', {
      url: "/:activeTab/:evnid/:ip/bundledetails",
      abstract: true,
      views: {
        'content': {
          templateUrl: "partials/bundle-detail.html",
          controller: "bundleDetailCtrl"
        }
      }
    })
    .state('app.bundledetails.info', {
      url: "/info/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/bundle-info.html",
          controller: "bundleInfoCtrl"
        }
      }
    })
    .state('app.bundledetails.loadConfig', {
      url: "/load-config/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/bundle-load-config.html",
          controller: "loadConfigCtrl"
        }
      }
    })
    .state('app.bundledetails.dependent', {
      url: "/dependent/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/bundle-dependent.html",
          controller: "bundleDependentCtrl"
        }
      }
    })
    .state('app.bundledetails.cxf', {
      url: "/cxf/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/cxf.html",
          controller: "cxfCtrl"
        }
      }
    })
    .state('app.bundledetails.checkVersion', {
      url: "/check-version/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/bundle-check-version.html",
          controller: "checkInfoCtrl"
        }
      }
    })
    .state('app.bundledetails.route', {
      url: "/route/:bid/:bname",
      views: {
        'content': {
          templateUrl: "partials/bundle-route.html",
          controller: "routeInfoCtrl"
        }
      }
    })
    .state('app.logs', {
      url: "/:activeTab/:evnid/:ip/logs",
      views: {
        'content': {
          templateUrl: "partials/logs.html",
          controller: "logsCtrl"
        }
      }
    })
    .state('app.Amq', {
      url: "/:activeTab/:evnid/:ip/Amq",
      abstract: true,
      views: {
        'content': {
          templateUrl: "partials/amq.html",
          controller: "amqCtrl"
        }
      }
    })
    .state('app.Amq.amqhome', {
      url: "/amqhome",
      views: {
        'content': {
          templateUrl: "partials/amq-home.html",
          controller: "homeCtrl"
        }
      }
    })
    .state('app.Amq.queue', {
      url: "/queue",
      views: {
        'content': {
          templateUrl: "partials/queue-home.html",
          controller: "queueCtrl"
        }
      },
      resolve: {
        Queuelist: function($http, $state, $rootScope, $stateParams) {
          showHideLoader('show');
          return $http.get(appurl + '/amq/home/' + $stateParams.ip).finally(function() {
            showHideLoader('hide');
          });
        }
      }
    })
    .state('app.Amq.topic', {
      url: "/topic",
      views: {
        'content': {
          templateUrl: "partials/topic-home.html",
          controller: "topicCtrl"
        }
      },
      resolve: {
        Queuelist: function($http, $state, $rootScope, $stateParams) {
          showHideLoader('show');
          return $http.get(appurl + '/amq/home/' + $stateParams.ip).finally(function() {
            showHideLoader('hide');
          });
        }
      }
    })
    .state('app.Msg', {
      url: "/:activeTab/:evnid/:ip/Msg/:objName",
      views: {
        'content': {
          templateUrl: "partials/msg.html",
          controller: "MsgCtrl"
        }
      }
    })
    .state('app.consumer', {
      url: "/:activeTab/:evnid/:ip/consumer/:Subscription",
      views: {
        'content': {
          templateUrl: "partials/consumer.html",
          controller: "ConsumerCtrl"
        }
      }
    })
    .state('app.subscriber', {
      url: "/:activeTab/:evnid/:ip/subscriber/:objName/:topicName",
      views: {
        'content': {
          templateUrl: "partials/subscriber.html",
          controller: "SubscriberCtrl"
        }
      }
    })

    .state('app.notfound', {
      url: "/notfound",
      views: {
        'content': {
          templateUrl: "partials/notfound.html",
          controller: function($scope, $cookieStore) {
            $cookieStore.remove("login");
            $cookieStore.remove("username");
            $cookieStore.remove("password")
          }
        }
      }
    })
    .state('app.errorfound', {
      url: "/errorfound",
      views: {
        'content': {
          templateUrl: "partials/errorfound.html",
          controller: function($scope, $cookieStore) {
            $cookieStore.remove("login");
            $cookieStore.remove("username");
            $cookieStore.remove("password")
          }
        }
      }
    })
    .state('auth', {
      url: "/auth",
      abstract: true,
      template: "<div ui-view></div>",
    })
    .state('auth.login', {
      url: "/login/:activeTab/:envid",
      templateUrl: "partials/login.html",
      controller: 'LoginCtrl'
    })
    .state('auth.logout', {
      url: "/logout",
      templateUrl: "partials/logout.html",
      controller: 'LogoutCtrl'
    });


  // if none of the above states are matched, use this as the fallback
  //  $urlRouterProvider.otherwise('/app/env');
}]);
mercuryApp.run(function($rootScope, $state, $stateParams, $cookieStore) {
  $rootScope.$on('$locationChangeSuccess', function(e, to) {
    //$rootScope.currentState = to;

    if ((to.indexOf($cookieStore.get("login")) <= -1 && to.indexOf('auth/login') == -1 && to.indexOf("app/notfound") == -1 && to.indexOf("app/errorfound") == -1) || (to.indexOf('auth/login') > -1 && !$rootScope.evnList)) {
      $state.go("app.env");
    }

  });

});

mercuryApp.controller("rootCtrl", ["$scope", function($scope) {


}]);



'use strict';
fameControllers.controller('AppCtrl', ['$scope', '$state', '$cookieStore', "$http", "$modal", "$rootScope", 'growl', '$location', '$stateParams', 'serverlist', function($scope, $state, $cookieStore, $http, $modal, $rootScope, growl, $location, $stateParams, serverlist) {

  if ($state.params.ip) {
    $rootScope.selectedServer = $rootScope.serverlist[_.findIndex($rootScope.serverlist, {
      "name": $state.params.ip
    })]
  }
  $rootScope.currentState = $state.current;
  $scope.activeTab = $stateParams.activeTab;
  $scope.evnid = $stateParams.evnid;
  $scope.userName = $cookieStore.get("username");

  $scope.cleartable = function() {

      $scope.userName = $cookieStore.remove("username");
      if (bundleListTable && bundleListTable.state) {
        bundleListTable.state.clear();

      }
    }
    // $scope.gotoServer = function(server) {
    //   console.log('evnid'+$stateParams.evnid);
    //   $location.path("/app/" + $stateParams.evnid + "/serverdashboard/" + server);
    // }
  var openModal = function(success) {
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: (!success) ? 'partials/popup-success.html' : 'partials/popup-error.html',
              size: 'sm',
      controller: function($scope, $modalInstance) {
        $scope.ok = function() {
          $modalInstance.close();
        }
        $scope.cancel = function() {
          $modalInstance.close();
        }
      }      
    });
  }


  $scope.setTestConnection = function(type) {
    showHideLoader('show');
    return $http.get(appurl + '/ds/' + type).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        growl.addWarnMessage("Successfully Connected to " + response.url);
      } else if (response.flag == false) {
        growl.addErrorMessage("Failed to Connect Due to " + response.message);
      }

    });
  }
  $scope.openDspopNew = function() {
    //  console.log('here');
    var modalInstance = $modal.open({
      templateUrl: 'partials/ds-list.html',
      scope: $scope,
      controller: function($scope, $modalInstance) {
        $scope.flagStatus = [];
        $scope.dataSouceNames = ["DATA1", "DATA2", "CENTRAL"]
        $scope.checkConnect = function() {
          showHideLoader('show');
          return $http.get(appurl + '/ds/testAS400Connection/' + $scope.dataSouceNames[$scope.dsOption]).success(function(response) {
            showHideLoader('hide');
            $scope.flagStatus[$scope.dsOption] = response.flag;
          });
        }

        $scope.ok = function() {
          $modalInstance.close();
        };

        $scope.cancel = function() {
          $modalInstance.dismiss('cancel');
        };

        $scope.delete = function() {
          $modalInstance.dismiss('cancel');
        };
      }


    })
  }


}]);

'use strict';

fameControllers.controller('AuthCtrl', ['$scope', function($scope) {


}]);
'use strict';

fameControllers.controller('LoginCtrl', ['$scope', '$state', '$cookieStore', '$location', '$rootScope', '$http', '$stateParams',
  function($scope, $state, $cookieStore, $location, $rootScope, $http, $stateParams) {
    $scope.loginFor = $rootScope.evnList[_.findIndex($rootScope.evnList, {
      "envName": $stateParams.envid,
    })];
    $scope.activeTab = $stateParams.activeTab;
    $scope.login = function() {

      $cookieStore.put("username", $scope.username);
      $cookieStore.put("login", $scope.loginFor.envName);
      $cookieStore.put("username", $scope.username);
      $cookieStore.put("password", $scope.password);
      $location.path("/app/envdetail/" + $scope.activeTab + "/" + ($scope.loginFor.envName ? $scope.loginFor.envName : $stateParams.envid));


    }
  }
]);

'use strict';

fameControllers.controller('LogoutCtrl', ['$scope', function($scope) {


}]);
'use strict';
fameControllers.controller('envCtrl', ['$scope', '$location', '$stateParams', '$http', '$rootScope', '$modal', '$cookieStore', 'growl', '$window',
  function($scope, $location, $stateParams, $http, $rootScope, $modal, $cookieStore, growl, $window) {
    $cookieStore.remove("login");
    $cookieStore.remove("username");
    $cookieStore.remove("password");

    showHideLoader('show');
    var localGroups = [];
    $scope.EvnNameError = null;
    $rootScope.evnList=null;
    $scope.AddEnvIcon = true;
    $scope.tabList = ["Fuse", "ActiveMQ"];
    $scope.activeTab = $scope.tabList[0];

    $scope.selectTab = function(tab) {
      $scope.activeTab = tab;

      $scope.responseTab();
    }
    //$cookieStore.put($scope.activeTab);
    $scope.responseTab = function() {
      $scope.EvnNameError = null;
      $http.get(appurl + "/envGroup/" + $scope.activeTab).success(function(response) {
        if (response.flag == true) {
          $scope.AddEnvIcon = true;
          $rootScope.envGroups = response.envGroups;
          localGroups = angular.copy($rootScope.envGroups);
          $scope.activeGroup = $scope.envGroups[0];
          if ($rootScope.envGroups.length == 0) {
            $scope.AddEnvIcon = false;
            showHideLoader('hide');
            return $scope.EvnNameError = 'No Environment Group available!';
          }
        } else if (response.flag == false) {
          $scope.EvnNameError = response.message;
        }

        showHideLoader('show');
        $http.get(appurl + "/envGroup/env/" + $scope.activeTab + "?envGrpName=" + $rootScope.envGroups[0]).success(function(response) {
          if (response.flag == true) {
            $rootScope.evnList = response.envServerList;
            if ($rootScope.evnList.length == 0) {
              $scope.EvnNameError = 'No Environment available';
            }
          } else if (response.flag == false) {

            $scope.EvnNameError = response.message;
          }

        }).finally(function() {
          showHideLoader('hide');
        });
      }).error(function() {
        showHideLoader('hide');
      });
    }
    $scope.responseTab();
    $scope.deleteGroup = function(data) {
      console.log(data);
      $scope.modalInstance = $modal.open({
        templateUrl: 'partials/deleteGroup.html',
        size: 'md',
        scope: $scope,
        controller: function($scope, $rootScope, $modalInstance) {

          $scope.delefunction = function() {
            console.log(data);
            $rootScope.removeGroup(data);

            $modalInstance.dismiss('cancel');
          }
          $scope.ok = function() {
            $modalInstance.close();
          };

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          };

          $scope.delete = function() {

            $modalInstance.dismiss('cancel');
          };
        }


      })
    }
    $rootScope.removeGroup = function(data, envGroup) {
      showHideLoader('show');
      $http.post(appurl + "/envGroup/delete/" + $scope.activeTab + "?envGrpName=" + $rootScope.envGroups[data]).success(function(response) {
        showHideLoader('hide');
        if (response.flag == true) {
          $rootScope.envGroups.splice(data, 1);
          $rootScope.evnList = angular.copy(data);
        //  $window.location.reload();
        //  $cookieStore.get($scope.activeTab);
          growl.addWarnMessage("Environment Group Deleted Successfully");
          $scope.activeGroup = $scope.envGroups[0];
          $scope.responseTab();
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    }
    $scope.saveGroup = function(data, oldG) {
      showHideLoader('show');
      $http.post(appurl + "/envGroup/update/" + $scope.activeTab + "?oldGrpName=" + oldG + "&newGrpName=" + data.name).success(function(response) {
        showHideLoader('hide');
        if (response.flag == true) {
          $rootScope.envGroups[$rootScope.envGroups.indexOf(oldG)] = data.name;
          growl.addWarnMessage("Environment Group Updated Successfully");

          if (oldG == $scope.activeGroup) {
            $scope.activeGroup = data.name;
          }
        }
        if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    }
    $scope.checkGroup = function(newG, oldGArr) {
      if (!newG) {
        growl.addErrorMessage("Must not be empty");
        return "";
      } else{
    	  for(var i=0;i<oldGArr.length;i++){
	    	  if (oldGArr[i] == newG) {
	    		  growl.addErrorMessage("Environment Group already Exist.");
	    		  return "";
	    	  }
          }
      }
      // if (id === 2 && data !== 'awesome') {
      //   return "Username 2 should be `awesome`";
      // }
    };
    $scope.addEnvGroup = function() {
      showHideLoader('show');
      $http.post(appurl + "/envGroup/add/" + $scope.activeTab + "?envGrpName=" + $scope.newEnvGroup).success(function(response) {
        $scope.AddEnvIcon = true;
        showHideLoader('hide');
        //  $window.location.reload();
        if (response.flag == true) {
          $rootScope.envGroups.push($scope.newEnvGroup);
          localGroups = angular.copy($rootScope.envGroups);
          $scope.newEnvGroup = '';
          growl.addWarnMessage("Environment Group Added Successfully");
          if ($scope.envGroups.length == 1) {
            $scope.activeGroup = $scope.envGroups[0];
          }
        }
        if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    }

    $scope.selectG = function(envGroup) {
      $scope.activeGroup = envGroup;
      showHideLoader('show');
      return $http.get(appurl + "/envGroup/env/" + $scope.activeTab + "?envGrpName=" + $scope.activeGroup).success(function(response) {
        if (response.flag == true) {
          $rootScope.evnList = response.envServerList;
          $scope.EvnNameError = null;
          if ($rootScope.evnList.length == 0) {
            $scope.EvnNameError = 'No Environment available';
          }
        } else if (response.flag == false) {

          $scope.EvnNameError = response.message;
        }

      }).finally(function() {
        showHideLoader('hide');
      });
    }
    $scope.addEnv = function(type, env) {
      var modalInstance = $modal.open({
        templateUrl: 'partials/addEnv.html',
        size: 'lg',
        controller: function($scope, $rootScope, $modalInstance) {

          if (type == 'edit') {

            $scope.envname = angular.copy(env.envName);
            $scope.ipList = angular.copy(env.serverList);

          } else if (type == 'add') {
            $scope.ipList = [];
          }

          $scope.checkIP = function(newip, oldip) {
            if (!newip) {
              return "Must not be empty";
            } else if (newip == oldip) {
              return "Must not same";
            } else if (!newip.match(/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}/)) {
              return "Must be in pattarn";
            } else if ($scope.ipList.indexOf(newip) > -1) {

              return "Already exist";
            }
          }
          $scope.checkVal = function(newval, oldval) {
            if (!newval) {
              return "Must not be empty";

            }
          }
          $scope.saveip = function(data, oldip) {
            console.log(data);
            $scope.ipList[$scope.ipList.indexOf(oldip)] = data.name;
          }
          $scope.deleteip = function(index) {
            $scope.ipList.splice(index, 1);
          }
          $scope.serverMsg = false;
          $scope.addServer = function(valid) {
            $scope.add_submitted = true;
            $scope.newVar = true;
            if (!valid) {
              return;
            }
            if ($scope.ipList.indexOf($scope.server) > -1) {
              return;
            }
            $scope.add_submitted = false;
            if ($scope.server) {
              $scope.ipList.unshift({
                  scheme: $scope.sche,
                  ip: $scope.server,
                  port: $scope.portserver,
                  jolokiaUrl: $scope.jolokia,
                })
                //$scope.ipList.unshift($scope.server);
              $scope.validList();
              $scope.server = '';
              $scope.portserver = '';
              $scope.jolokia = '';
            }

          };


          $scope.validList = function() {
            if (!$scope.ipList || $scope.ipList.length == 0) {
              $scope.listMsg = "Add atleast one server"

              return false;
            }

            else {
              $scope.listMsg = ""
              return true
            }
          }

          $scope.validEnv = function() {
            if (!$scope.envname) {
              $scope.envnameMsg = "must not be empty"

              return false;
            } else {
              $scope.envnameMsg = ""
              return true
            }
          }
          $scope.ok = function() {
            if (!$scope.validEnv()) {
              return
            }
            if (!$scope.validList()) {
              return
            }
            $modalInstance.close({

              "envName": $scope.envname,
              "serverList": $scope.ipList

            });

          };

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          };

          $scope.delete = function() {

            $modalInstance.dismiss('cancel');
          };
        },




      })
      modalInstance.result.then(function(data) {
        console.log(data.serverList);
        showHideLoader('show');
        $scope.postData = [];
        _.forEach(data.serverList, function(i) {
          $scope.postData.push({
            "scheme": i.scheme,
            "ip": i.ip,
            "port": i.port,
            "jolokiaUrl": i.jolokiaUrl,
            "type": $scope.activeTab
          })
        })
        if (type == 'edit') {
          return $http({
            method: 'POST',
            url: appurl + "/envGroup/env/update/" + $scope.activeTab + "?envGrpName=" + $scope.activeGroup + "&oldEnvName=" + env.envName + "&newEnvName=" + data.envName,
            data: $scope.postData
          }).success(function(response) {
            if (response.flag == true) {
              var index = _.findIndex($rootScope.evnList, {
                "envName": env.envName
              });
              $rootScope.evnList[index] = data;
              growl.addWarnMessage("Environment Updated Successfully.");
            } else if (response.flag == false) {
              growl.addErrorMessage(response.message);
            }
          }).finally(function() {
            showHideLoader('hide');
          });
        } else {

          return $http({
            method: 'POST',
            url: appurl + "/envGroup/env/add/" + $scope.activeTab + "?envGrpName=" + $scope.activeGroup + "&envName=" + data.envName,
            data: $scope.postData
          }).success(function(response) {

            $scope.EvnNameError = null;
            if (response.flag == true) {
              if($rootScope.evnList==[0]){
                $window.location.reload();
                $rootScope.evnList.push(data);
                growl.addWarnMessage("Environment Added Successfully.");
              } else{
                $rootScope.evnList.push(data);
                growl.addWarnMessage("Environment Added Successfully.");
              }

            } else if (response.flag == false) {
              growl.addErrorMessage(response.message);
            }
          }).finally(function() {
            showHideLoader('hide');
          });
        }
      })
    }
    $scope.deleteEnv = function(envName, $index) {
      $scope.modalInstance = $modal.open({
        templateUrl: 'partials/deleteEnv.html',
        size: 'md',
        scope: $scope,
        controller: function($scope, $rootScope, $modalInstance) {

          $scope.delefunction = function() {
            $rootScope.envDelete(envName, $index);
            $modalInstance.dismiss('cancel');
          }
          $scope.ok = function() {
            $modalInstance.close();
          };

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          };

          $scope.delete = function() {

            $modalInstance.dismiss('cancel');
          };
        }


      })
    }
    $rootScope.envDelete = function(envName, index) {
      showHideLoader('show');
      return $http.post(appurl + "/envGroup/env/delete/" + $scope.activeTab + "?envGrpName=" + $scope.activeGroup + "&envName=" + envName).success(function(response) {

        showHideLoader('hide');
        if (response.flag == true) {
          $scope.evnList.splice(index, 1);
          growl.addWarnMessage("Environment Deleted Successfully.");
          if ($scope.evnList.length == 0) {
            $scope.EvnNameError = 'No Environment available';
          }
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });

    }
    $scope.go = function(index) {

      console.log("fdfgdhjgfds");
      $scope.pathenv = $scope.activeTab + "/" + index;
      console.log();
      $location.path("/auth/login/" + $scope.pathenv);
    }
    setTimeout(function() {
      var h = ($(window).height());
      $('#sidebar-wrapper').css({
        'height': (h / 1.45)
      });
      $(window).resize(function() {
        $('#sidebar-wrapper').css({
          'height': (h / 1.45)
        });
      });
    });

  }
]);
fameControllers.controller('headerdetailCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;

}]);
var bundleListTable = null;
fameControllers.controller('reportCtrl', ['$scope', '$rootScope', '$stateParams', '$http', '$modal', '$q', '$location', function($scope, $rootScope, $stateParams, $http, $modal, $q, $location) {
  $scope.qzGroup=false;
  $scope.dsList=false;
  $scope.bundleTable=false;
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];
  $scope.gotoServer = function(server) {
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $rootScope.selectedServer = $scope.serverlist[_.findIndex($scope.serverlist, {
      "name": $scope.ip
    })]
    //  "chartdivHeap"
  var calPercent = function(used, max) {
    return parseInt((used / max) * 100)
  }

  function addZero(i) {
    if (i < 10) {
      i = "0" + i;
    }
    return i;
  }

  function formatDate() {
    var timeByType = new Date();
    var d = addZero(timeByType.getDate());
    var month = new Array();
    month[0] = "January";
    month[1] = "February";
    month[2] = "March";
    month[3] = "April";
    month[4] = "May";
    month[5] = "June";
    month[6] = "July";
    month[7] = "August";
    month[8] = "September";
    month[9] = "October";
    month[10] = "November";
    month[11] = "December";
    var mon = month[timeByType.getMonth()];
    var y = addZero(timeByType.getFullYear());
    var h = addZero(timeByType.getHours());
    var m = addZero(timeByType.getMinutes());
    var s = addZero(timeByType.getSeconds());
    return (h + ":" + m + ":" + s);

  }
  var createChart = function(response) {
    var timeByType = formatDate()
    $scope.chart = AmCharts.makeChart("chartdiv", {
      "type": "serial",
      "theme": "light",
      "legend": {
        "useGraphSettings": true
      },
      "dataProvider": [{
        "period": timeByType,
        "Heap": calPercent(response.heapMemory.used, response.heapMemory.committed),
        "Non Heap": calPercent(response.nonHeapMemory.used, response.nonHeapMemory.committed)
      }],
      "valueAxes": [{
        "integersOnly": true,
        "axisAlpha": 0,
        "dashLength": 5,
        "gridCount": 10,
        "position": "left",
        "title": "Place taken"
      }],
      "startDuration": 0.5,
      "graphs": [{
        "balloonText": "<b>[[category]]: [[value]]</b>",
        "fillColorsField": "color",
        "fillAlphas": 0.9,
        "lineAlpha": 0.2,
        "type": "column",
        "valueField": "Heap"
      }, {
        "balloonText": "<b>[[category]]: [[value]]</b>",
        "fillColorsField": "color",
        "fillAlphas": 0.9,
        "lineAlpha": 0.2,
        "type": "column",
        "valueField": "Non Heap"
      }],
      "chartCursor": {
        "cursorAlpha": 0,
        "zoomable": false
      },
      "categoryField": "period",
      "categoryAxis": {
        "gridPosition": "start",
        "axisAlpha": 0,
        "fillAlpha": 0.05,
        "fillColor": "#000000",
        "gridAlpha": 0,
        "position": "top"
      },
      "export": {
        "enabled": true,
        "position": "bottom-right"
      }
    });

  }
  var setBundleList = function(ip) {
    $http.get(appurl + '/memory/allMemory/' + ip).success(function(response) {});

    return $http.get(appurl + '/bundle/list/' + ip).success(function(response) {

      if (response.flag == true) {
        //  $scope.bundleList=null;
        $scope.bundleTable=true;

        bundleListTable = $('#bundleList').DataTable({
          destroy: true,
          "search": {
            "search": "com.cox.bis",
          },
          "bStateSave": true,

          "data": response.bundleList,
          "columns": [{
            "data": "ID"
          }, {
            "data": "Name"
          }, {
            "data": "Version"
          }, {
            "data": "State"
          }, {
            "data": "Blueprint"
          }, {
            "data": "Blueprint"
          }],
          "columnDefs": [{
              "targets": 3,
              "data": "State",
              "orderable": false,
              "render": function(data) {
                return '<i class="fa fa-circle ' + (data == "ACTIVE" ? 'active' : '') + '" title=' + data + '></i>';
              }
            }, {
              "targets": 4,
              "data": "Blueprint",
              "orderable": false,
              "render": function(data) {
                return '<i class="fa fa-circle ' + (data == "Created" ? 'active' : (data == "Failure" ? 'text-danger' : '')) + '" title=' + (data == '' ? "N/A" : data) + '></i>';
              }
            }, {
              "targets": 5,
              "data": "Blueprint",
              "orderable": false,
              "render": function(data, type, full) {

                return '<a title="Action" href="#/app/'+ $scope.activeTab + '/' + $stateParams.evnid + '/' + $stateParams.ip + '/bundledetails/info/' + full.ID + '/' + full.Name + '"><i class="fa fa-external-link"></i></a>';

              }
            }

          ]

        });

      } else {
        $scope.BundleListError = response.message;
      }
    });




  };
  $scope.refresh = function(type) {
    showHideLoader('show')
    var promise = null;
    if (type == 'ds') {
      promise = setDSList($stateParams.ip);
    } else if ('qz') {
      promise = setQuartz($stateParams.ip);
    }
    promise.finally(function() {
      showHideLoader('hide')
    });
  }

  $scope.openDsPopup = function() {

    var modalInstance = $modal.open({
      templateUrl: 'partials/dbinfo.html',
      scope: $scope,
      controller: function($scope, $rootScope, $modalInstance) {
        //  console.log($scope);
        $scope.dataSourceInfo = function() {};

        $scope.ok = function() {
          $modalInstance.close();
        };

        $scope.cancel = function() {
          $modalInstance.dismiss('cancel');
        };

        $scope.delete = function() {
          $modalInstance.dismiss('cancel');
        };
      }


    })
  };
  var openModal = function(success) {
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: (!success) ? 'partials/popup-success.html' : 'partials/popup-error.html',
              size: 'sm',
      controller: function($scope, $modalInstance) {
        $scope.ok = function() {
          $modalInstance.close();
        }
        $scope.cancel = function() {
          $modalInstance.close();
        }
      }      
    });
  }

  var setDSList = function(ip) {
      return $http.get(appurl + '/ds/getDSCount/' + ip).success(function(response) {
        // response={"dsGroup":{"SQL_SERVER":2},"message":null,"flag":true}

        if (response.flag) {

          $scope.dsList = response.dsGroup;
          $scope.dserror = null;
        } else {
          $scope.dserror = response.message;
        }
      });
    }
    //http://localhost:8080/StatisticsApi/ds/getDSDependent/com.cox.bis.enterpriseservices:type=BasicDataSource
  var openModal = function(success) {
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: (!success) ? 'partials/popup-success.html' : 'partials/popup-error.html',
              size: 'sm',
      controller: function($scope, $modalInstance) {
        $scope.ok = function() {
          $modalInstance.close();
        }
        $scope.cancel = function() {
          $modalInstance.close();
        }
      }      
    });
  }
  $scope.dsDependent = function(type) {
    return $http.get(appurl + '/ds/testConnection/' + type).success(function(response) {
      growl.addWarnMessage(response.url);
    });
  }
  var setQuartz = function(ip) {
    return $http.get(appurl + '/quartz/getSchedulerCount/' + ip).success(function(response) {
      //response={"schedulerGroup":{"Simple":4,"Oricle":6},"message":null,"flag":true}
      if (response.flag) {
        $scope.qzGroup = response.schedulerGroup;
        $scope.qzerror = null;
      } else {
        $scope.qzerror = response.message;
      }
    });
  }


  // if (!$rootScope.serverlist) {
  //
  //   $http.get(appurl + '/home/loadServers/' + $stateParams.evnid)
  // } else {
  //   $scope.severDetails = _.where($rootScope.serverlist, {
  //     "name": "" + $stateParams.ip
  //   })[0];
  // }
  var callCart = function(ip) {
    return $http.get(appurl + '/memory/allMemory/' + ip).success(function(response) {
      createChart(response);

    });
  }
  $scope.refreshLists = function(ip) {
    showHideLoader('show');
    var promise4 = callCart(ip);
    var promise1 = setBundleList(ip);
    var promise2 = setDSList(ip);
    var promise3 = setQuartz(ip);

    $q.all([promise4, promise1, promise2, promise3]).finally(function() {
      showHideLoader('hide');
    });
  }
  $scope.refreshLists($stateParams.ip);

  intervelId = setInterval(function() {
    callCart($stateParams.ip);
  }, 15000);
  $scope.$on("$destroy", function() {
    clearInterval(intervelId)
  });
}]);

'use strict';

fameControllers.controller('serversCtrl', ['$scope', '$http', '$stateParams', '$rootScope', function($scope, $http, $stateParams, $rootScope) {
  $scope.envid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  if ($scope.activeTab == 'ActiveMQ') {
    $scope.countDetail = false;
  } else {
    $scope.countDetail = true;
  }
  //showHideLoader('show');
  // $http.get(appurl + '/home/loadServers/' + $stateParams.evnid).success(function(response) {
  //   //response={"serverInfoList":[{"name":"127.0.0.1","pingStatus":true,"dbCount":2,"bundleCount":6},{"name":"10.51.5.139","pingStatus":true,"dbCount":2,"bundleCount":0},{"name":"10.62.62.212","pingStatus":true,"dbCount":6,"bundleCount":0},{"name":"10.62.60.121","pingStatus":false,"dbCount":0,"bundleCount":0}],"flag":true,"message":null}
  //   if (response.flag) {
  //     $rootScope.serverlist = response.serverInfoList;
  //     showHideLoader('hide');
  //   } else {
  //     $scope.alert.push(response.message);
  //   }
  // });
}]);

fameControllers.controller('bundleDetailCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', function($scope, $http, $stateParams, $rootScope, $modal, $location) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  if (!$rootScope.serverlist) {
    $http.get(appurl + '/home/loadServers/' + $stateParams.evnid).success(function(response) {
      if (response.flag) {
        $rootScope.serverlist = response.serverInfoList;
        $scope.severDetails = _.where($rootScope.serverlist, {
          "name": "" + $stateParams.ip
        })[0];
      }
    });
  } else {
    $scope.severDetails = _.where($rootScope.serverlist, {
      "name": "" + $stateParams.ip
    })[0];
  }



  $scope.go = function(type) {
    console.log("here");
    $location.path("/app/"+ $scope.activeTab + "/" + $stateParams.evnid + "/" + $stateParams.ip + "/bundledetails/" + type + "/" + $scope.bid + "/" + $scope.bname);
  }

}]);
fameControllers.controller('bundleInfoCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', 'growl', function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, growl) {
  showHideLoader('show');
  $scope.$parent.bname = $stateParams.bname;
  $scope.$parent.bid = $stateParams.bid;
$scope.activeTab = $stateParams.activeTab;
  $http.get(appurl + '/bundle/getBundleInfo/' + $stateParams.bid).success(function(response) {
    showHideLoader('show');
    var main = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    tab2.removeClass('active');
    var tab3 = angular.element(document.querySelector('#myTab li:nth-child(4)'));
    tab3.removeClass('active');
    var tab4 = angular.element(document.querySelector('#myTab li:nth-child(5)'));
    tab4.removeClass('active');
    var tab5 = angular.element(document.querySelector('#myTab li:nth-child(6)'));
    tab5.removeClass('active');
    if (response.flag) {
      $scope.data = response;
      $scope.BundleInfoError = null;
      if ($scope.data.length == 0) {
        $scope.BundleInfoError = 'No record found';
      }
      showHideLoader('hide');
    } else {
      $scope.BundleInfoError = response.message;
      showHideLoader('hide');
    }
  });
  $scope.bundleState = function(type) {
    console.log('herew');
    showHideLoader('show');

    $http.get(appurl + '/bundle/changeBundleState?bundleId=' + $stateParams.bid + '&state=' + type).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        $http.get(appurl + '/bundle/getBundleInfo/' + $stateParams.bid).success(function(response) {
          showHideLoader('show');
          if (response.flag) {
            $scope.data = response;
            $scope.BundleInfoError = null;
            if ($scope.data.length == 0) {
              $scope.BundleInfoError = 'No record found';
            }
            showHideLoader('hide');
          } else {
            $scope.BundleInfoError = response.message;
            showHideLoader('hide');
          }
        });
        growl.addWarnMessage(response.state + " Successfully");
      } else {
        growl.addErrorMessage("Failed Due to: " + response.message);
        $scope.bundleStateError = response;
        $timeout(function() {
          $scope.bundleStateError = null;
        }, 5000);
        showHideLoader('hide');

      }
    });
  }
}]);
//http://localhost:8080/StatisticsApi/quartz/allTrigger/quartz:instance=NON_CLUSTERED,name=DefaultQuartzScheduler-camel-cxf-contract-first-blueprint,type=QuartzScheduler
fameControllers.controller('quartzSchedulerCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', 'growl', function($scope, $http, $stateParams, $rootScope, $modal, $location, growl) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  showHideLoader('show');
  $scope.isopen = [];
  $scope.isopen[0] = true;
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];
  $scope.schList = [];
  $scope.obj = {};
  $http.get(appurl + '/quartz/getSchedulerList/').success(function(response) {
    //response={"schedulerNameList":[{"type":"SIMPLE","names":["quartz:instance=dukeewdv95.corp.cox.com1470395945656,name=DatabaseClusteredScheduler-com.cox.bis.addresscleansingjob,type=QuartzScheduler","quartz:instance=dukeewdv95.corp.cox.com1470396013977,name=DatabaseClusteredScheduler-com.cox.bis.fccpublicfileservicesjob,type=QuartzScheduler"]},{"type":"Oricle","names":["quartz:instance=dukeewdv95.corp.cox.com1470395945656,name=DatabaseClusteredScheduler-com.cox.bis.addresscleansingjob,type=QuartzScheduler","quartz:instance=dukeewdv95.corp.cox.com1470396013977,name=DatabaseClusteredScheduler-com.cox.bis.fccpublicfileservicesjob,type=QuartzScheduler"]}],"flag":true,"message":null}
    if (response.flag) {
      $scope.schList = _.uniq(response.schedulerNameList);

      $scope.schedulerListError = null;
      if ($scope.schList.length == 0) {
        $scope.schedulerListError = 'No record found';
      }
      showHideLoader('hide');
    } else {
      $scope.schedulerListError = response.message;
      showHideLoader('hide');
    }
  }).finally(function() {
    showHideLoader('hide');
  });
  $scope.schPlayPause = function(type) {
    showHideLoader('show');
    $http.get(appurl + '/quartz/playOrPauseScheduler/' + $scope.tableshow + '/' + type).success(function(response) {
      showHideLoader('hide');
      $scope.detail.Started = !$scope.detail.Started;
      //$scope.notification = response.state;
      //popModal();
      if (response.flag == true) {
        growl.addWarnMessage(response.state + " Successfully");
      } else if (response.flag == false) {
        growl.addErrorMessage("Failed Due to " + response.message);
      }

    });
  }
  $scope.getSchDetails = function(sch, fromtab, index) {
    $scope.selected = index;

    if ($scope.showType == 'details' && $scope.tableshow && $scope.tableshow == sch) {
      return;
    }
    $scope.schedulerDetailError = null;
    $scope.detail = null
    $scope.showType = 'details';
    $scope.tableshow = sch
    showHideLoader('show');
    $http.get(appurl + '/quartz/getSchedulerDetail/' + sch).success(function(response) {
      //  response={"schedulerInfo":{"SchedulerName":"DatabaseClusteredScheduler-com.cox.bis.addresscleansingjob","SchedulerInstanceId":"dukeewdv95.corp.cox.com1470395945656","Version":"2.2.1","Started":true,"SampledStatisticsEnabled":false,"JobStoreClassName":"org.quartz.impl.jdbcjobstore.JobStoreTX","ThreadPoolClassName":"org.quartz.simpl.SimpleThreadPool","ThreadPoolSize":"1"},"message":null,"flag":true}
      if (response.flag) {
        $scope.detail = response.schedulerInfo;
        if ($scope.detail.length == 0) {
          $scope.schedulerDetailError = 'No record found';
        }
        showHideLoader('hide');
      } else {
        $scope.schedulerDetailError = response.message;
        showHideLoader('hide');
      }
    }).finally(function() {
      showHideLoader('hide');
    });
  }
  $scope.obj = {}
  $scope.getTrigger = function(sch) {
    if ($scope.showType == 'trigger') {
      return;
    }

    $scope.showType = 'trigger';
    tiggerService();

  }

  var tiggerService = function() {
    $scope.triggerDetailsError = null;
    $scope.obj.selectedTg = null
    $scope.triggerDetails = null;
    showHideLoader('show');
    $http.get(appurl + '/quartz/allTrigger/' + $scope.obj.selectedSch).success(function(response) {
      if (response.flag) {
        $scope.triggerDetails = response.triggerDetailList;

        _.forEach($scope.triggerDetails, function(n) {
          n.nextFireTime = new Date(n.nextFireTime);
          // n.previousFireTime = new Date(n.previousFireTime);
          n.startTime = new Date(n.startTime);
        });
        if ($scope.triggerDetails.length == 0) {
          $scope.triggerDetailsError = 'No record found';
        }

        showHideLoader('hide');
      } else {
        $scope.triggerDetailsError = response.message;
        showHideLoader('hide');
      }

    }).finally(function() {
      showHideLoader('hide');
    });
  }
  $scope.pause = function() {
    showHideLoader('show');
    return $http.get(appurl + '/quartz/pauseTrigger/' + $scope.tableshow + '/' + $scope.obj.selectedTg.jobName + '/' + $scope.obj.selectedTg.group).success(function(response) {
      $scope.obj.selectedTg.state = "PAUSED"
      $scope.notification = response.state;
      //popModal($scope.notification);
      if (response.flag == true) {
        growl.addWarnMessage($scope.notification + " Successfully");
      } else if (response.flag == false) {
        growl.addErrorMessage("Failed Due to " + response.message);
      }
    }).finally(function() {
      showHideLoader('hide');
    });

  }
  $scope.resume = function() {
    showHideLoader('show');
    return $http.get(appurl + '/quartz/resumeTrigger/' + $scope.tableshow + '/' + $scope.obj.selectedTg.jobName + '/' + $scope.obj.selectedTg.group).success(function(response) {
      $scope.obj.selectedTg.state = "NORMAL"
      $scope.notification = response.state;
      //popModal($scope.notification);
      if (response.flag == true) {
        growl.addWarnMessage($scope.notification + " Successfully");
      } else if (response.flag == false) {
        growl.addErrorMessage("Failed Due to " + response.message);
      }
    }).finally(function() {
      showHideLoader('hide');
    });

  }
  $scope.cronModal = function() {
    $scope.obj.outputExp = null;
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: 'partials/cron.html',
      scope: $scope,
              size: 'md',
      controller: function($scope, $modalInstance) {

        setTimeout(function() {

          $(function() {

            $("#cron").cronGen();
            console.log("set :" + $scope.obj.selectedTg.jobDataMap.CamelQuartzTriggerCronExpression);

            $("#cron").val($scope.obj.selectedTg.jobDataMap.CamelQuartzTriggerCronExpression);
          });
        })
        $scope.ok = function() {
          $modalInstance.close({
            "val": $("#cron").val()
          });
        }
        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      }      
    });
    modalInstance.result.then(function(data) {
      showHideLoader('show');
      $http.get(appurl + '/quartz/updateTrigger?objName=' + $scope.tableshow + '&jobName=' + $scope.obj.selectedTg.jobName + '&groupName=' + $scope.obj.selectedTg.group + '&misfireInstruction=' + $scope.obj.selectedTg.misfireInstruction + '&cronExpression=' + data.val).success(function(response) {
        //   response={"state":"Updated","flag":true,"message":null}
        $scope.obj.selectedTg.jobDataMap.CamelQuartzTriggerCronExpression = data.val;
        if (response.flag == true) {
          tiggerService();
          growl.addWarnMessage(response.state + " Successfully");
        } else if (response.flag == false) {
          growl.addErrorMessage("Failed Due to " + response.message);
        }
      }).finally(function() {
        $scope.obj.selectedTg.jobDataMap.CamelQuartzTriggerCronExpression = data.val;
        showHideLoader('hide');

      });
    })
  }
  var popModal = function() {
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: 'partials/notification.html',
      scope: $scope,
              size: 'sm',
      controller: function($scope, $modalInstance) {
        $scope.ok = function() {
          $modalInstance.close();
        }
        $scope.cancel = function() {
          $modalInstance.close();
        }
      }      
    });
  }
}]);
//URL: http://localhost:8080/StatisticsApi/bundle/getRoute/com.cox.bis.eSignService
fameControllers.controller('routeInfoCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', function($scope, $http, $stateParams, $rootScope, $modal, $location) {
  showHideLoader('show');
  $scope.$parent.bname = $stateParams.bname;
  $scope.$parent.bid = $stateParams.bid;
  $scope.activeTab = $stateParams.activeTab;
  $http.get(appurl + '/bundle/getRoute/' + $stateParams.bname).success(function(response) {
    var main = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    tab2.removeClass('active');
    var tab3 = angular.element(document.querySelector('#myTab li:nth-child(4)'));
    tab3.removeClass('active');
    var tab4 = angular.element(document.querySelector('#myTab li:nth-child(5)'));
    tab4.removeClass('active');
    var tab5 = angular.element(document.querySelector('#myTab li:nth-child(6)'));
    tab5.removeClass('active');
    if (response.flag) {
      $scope.routeList = response.routeDefinitionResponse;
      $scope.routeError = null;
      if ($scope.routeList.length == 0) {
        $scope.routeError = 'No record found';
      }
      showHideLoader('hide');
    } else {
      $scope.routeError = response.message;
      showHideLoader('hide');
    }
  });

}]);
//URL: http://localhost:8080/StatisticsApi/bundle/checkVersion/org.jruby.jruby
fameControllers.controller('checkInfoCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', function($scope, $http, $stateParams, $rootScope, $modal, $location) {
  showHideLoader('show');
  $scope.$parent.bname = $stateParams.bname;
  $scope.$parent.bid = $stateParams.bid;
  $http.get(appurl + '/bundle/checkVersion/' + $stateParams.bname).success(function(response) {
    var main = angular.element(document.querySelector('#myTab li:nth-child(4)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    tab2.removeClass('active');
    var tab3 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    tab3.removeClass('active');
    var tab4 = angular.element(document.querySelector('#myTab li:nth-child(5)'));
    tab4.removeClass('active');
    var tab5 = angular.element(document.querySelector('#myTab li:nth-child(6)'));
    tab5.removeClass('active');
    if (response.flag) {

      $scope.versionList = response.bundleVersionList;
      $scope.versionError = null;
      if ($scope.versionList.length == 0) {
        $scope.versionError = 'No record found';
      }
      showHideLoader('hide');
    } else {
      $scope.versionError = response.message;
      showHideLoader('hide');
    }
  });

}]);
//URL: http://localhost:8080/StatisticsApi/bundle/loadConfig/org.jolokia.osgi
fameControllers.controller('loadConfigCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', 'growl', function($scope, $http, $stateParams, $rootScope, $modal, $location, growl) {
  showHideLoader('show');
  $scope.$parent.bname = $stateParams.bname;
  $scope.obj = {};
  $scope.$parent.bid = $stateParams.bid;
  $http.get(appurl + '/bundle/getAllConfig/' + $stateParams.bname).success(function(response) {
    // response={"configList":["com.cox.bis.enterpriseservices.v2"],"message":null,"flag":true}
    var main = angular.element(document.querySelector('#myTab li:nth-child(5)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    tab2.removeClass('active');
    var tab3 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    tab3.removeClass('active');
    var tab4 = angular.element(document.querySelector('#myTab li:nth-child(4)'));
    tab4.removeClass('active');
    var tab5 = angular.element(document.querySelector('#myTab li:nth-child(6)'));
    tab5.removeClass('active');
    if (response.flag) {

      $scope.configList = response.configList;
      $scope.configError = null;
      if ($scope.configList.length == 0) {
        $scope.configError = 'No record found';
      }
      showHideLoader('hide');
    } else {
      $scope.configError = response.message;
      showHideLoader('hide');
    }
  });
  $scope.configprop = function(data) {
    $scope.editable = true;
    $scope.configEdit = true;
    $scope.configSave = false;
    $scope.configAdd = false;
    $scope.configCancel = false;
    $scope.configPropError = null;
    if ($scope.tableshow != data) {
      $scope.configPropList = null;
      $scope.tableshow = data;
      showHideLoader('show');
      $http.get(appurl + '/bundle/loadConfig/' + data).success(function(response) {
        //    response={"configPropList":[{"name":"enterpriseservices.removeAbandonedOnBorrow","value":"true"},{"name":"enterpriseservices.minIdle","value":"10"},{"name":"enterpriseservices.url","value":"jdbc:sqlserver://catl0db157:1500;instanceName=prodlike2;DatabaseName=EnterpriseServices;loginTimeout=5;socketTimeout=120"},{"name":"enterpriseservices.maxOpenPreparedStatements","value":"100"},{"name":"enterpriseservices.poolPreparedStatements","value":"true"},{"name":"felix.fileinstall.filename","value":"file:/opt/app/fuse/jboss-fuse-6.2.1.redhat-090/etc/com.cox.bis.enterpriseservices.v2.cfg"},{"name":"enterpriseservices.logAbandoned","value":"true"},{"name":"enterpriseservices.maxWaitMillis","value":"1000"},{"name":"enterpriseservices.maxTotal","value":"200"},{"name":"enterpriseservices.username","value":"EnterpriseServices_User"},{"name":"enterpriseservices.testOnBorrow","value":"true"},{"name":"enterpriseservices.testOnReturn","value":"true"},{"name":"enterpriseservices.testWhileIdle","value":"true"},{"name":"servicename","value":"enterpriseservices"},{"name":"enterpriseservices.validationQueryTimeout","value":"500"},{"name":"enterpriseservices.validationQuery","value":"select 1"},{"name":"enterpriseservices.timeBetweenEvictionRunsMillis","value":"15000"},{"name":"enterpriseservices.maxIdle","value":"20"},{"name":"enterpriseservices.numTestsPerEvictionRun","value":"10"},{"name":"enterpriseservices.minEvictableIdleTimeMillis","value":"30000"},{"name":"service.pid","value":"com.cox.bis.enterpriseservices.v2"},{"name":"enterpriseservices.driverClassName","value":"com.microsoft.sqlserver.jdbc.SQLServerDriver"},{"name":"enterpriseservices.maxConnLifetimeMillis","value":"600000"},{"name":"enterpriseservices.removeAbandonedTimeout","value":"30000"},{"name":"enterpriseservices.password","value":"prodlike"}],"flag":true,"message":null}
        if (response.flag) {
          $scope.configPropList = response.configPropList;
          $scope.configPropError = null;
          showHideLoader('hide');
        } else {
          $scope.configPropError = response.message;
          showHideLoader('hide');
        }
      });
    }
  }
  var configPropList = null;
  $scope.editConfig = function() {
    configPropList = angular.copy($scope.configPropList);
    $scope.editable = false;
    $scope.configEdit = false;
    $scope.configSave = true;
    $scope.configAdd = true;
    $scope.configCancel = true;
  }
  $scope.saveConfig = function(status) {
    showHideLoader('show');
    var postData = angular.toJson($scope.configPropList);
    $http.post(appurl + '/bundle/updateConfig?bundleName=' + $scope.tableshow, postData).success(function(response) {
      //showHideLoader('hide');
      if (response.flag == true) {
        //$scope.tableshow = postData;
        showHideLoader('show');
        $http.get(appurl + '/bundle/loadConfig/' + $scope.tableshow).success(function(response) {

          $scope.editable = true;
          $scope.configEdit = true;
          $scope.configSave = false;
          $scope.configAdd = false;
          $scope.configCancel = false;
          if (response.flag) {
            $scope.configPropList = response.configPropList;
            $scope.configPropError = null;

          } else {
            $scope.configPropError = response.message;
          }
        }).finally(function() {
          showHideLoader('hide');
        });
        if (status == 'Add') {
          growl.addWarnMessage("Configuration Added Successfully!");
        } else if (status == 'Delete') {
          growl.addWarnMessage("Configuration Deleted Successfully!");
        } else {
          growl.addWarnMessage("Configuration Updated Successfully!");
        }
      } else if (response.flag == false) {
        growl.addErrorMessage(response.message);
      }
    }).finally(function() {
      showHideLoader('hide');
    });
  }
  $scope.cancelConfig = function() {
    //	  $scope.editable=false;
    $scope.configEdit = true;
    $scope.configSave = false;
    $scope.configAdd = false;
    $scope.configCancel = false;
    if (configPropList) {
      $scope.configPropList = angular.copy(configPropList);
    }
    $scope.editable = true;
  }
  $scope.addConfig = function() {
      var modalInstance = $modal.open({        
        animation: true,
        templateUrl: 'partials/addConfig.html',
        size: 'md',
        controller: function($scope, $modalInstance) {
          $scope.addRow = function() {
            $modalInstance.close({
              name: $scope.name,
              value: $scope.value
            });
          };

          $scope.cancel = function() {
            $modalInstance.dismiss();
          }
        }      
      });
      modalInstance.result.then(function(data) {
        $scope.configPropList.push(data);
        $scope.saveConfig('Add');
      });

    }
    // $scope.deleteConfig=function(data){
    //    $scope.configPropList.splice(data,1)
    //  }
  $scope.deleteConfig = function(data) {
    $scope.configName = [];
    var modalInstance = $modal.open({        
      animation: true,
      templateUrl: 'partials/deleteConfig.html',
      size: 'md',
      controller: function($scope, $modalInstance, configPropList) {
        $scope.configPropList = configPropList;
        $scope.deleteRow = function() {
          $modalInstance.close($scope.config);
        };

        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      },
      resolve: {
        configPropList: function() {
          return $scope.configPropList;
        }
      }      
    });
    modalInstance.result.then(function(data) {
      var index = _.findIndex($scope.configPropList, {
        "name": data.name
      })
      $scope.configPropList[index].value = null;
      $scope.saveConfig('Delete');
    });

  }
}]);
// fameControllers.controller('loadConfigCtrl', ['$scope','$http', '$stateParams','$rootScope', '$modal','$location', function($scope, $http, $stateParams, $rootScope, $modal, $location) {
//   showHideLoader('show');
//     $scope.$parent.bname=$stateParams.bname;
//     //$scope.$parent.bid=$stateParams.bid;
//     $http.get(appurl+'/bundle/loadConfig/'+$stateParams.bname).success(function(response) {
//       if(response.flag){
//         $scope.configList=response.configPropList;
//         $scope.configError = null;
//         if($scope.configList.length==0) {
//           $scope.configError = 'No record found';
//         }
//         showHideLoader('hide');
//       }else{
//         $scope.configError = response.message;
//         showHideLoader('hide');
//       }
//    });
//
//   }
// ]);
// var openModal= function () {
//   console.log("done");
//
//   var modalInstance = $modal.open({
//         animation: true,
//         templateUrl:'partials/dbinfo.html',
//         size: 'md',
//       controller : function ($scope,$modalInstance) {
//         $scope.dataSourceInfo = function (type) {
//           console.log("yea");
//           return $http.get(appurl+'/getDbInfo/'+type).success( function(response) {
//             openModal(response);
//          });}
//          $scope.ok= function () {
//            $modalInstance.close();
//          }
//          $scope.cancel= function () {
//            $modalInstance.close();
//          }
//        }
//      });
//    }
//       



//http://localhost:8080/StatisticsApi/ds/getDbInfo/com.cox.bis.enterpriseservices:type=BasicDataSource

//  fameControllers.controller('bundleInfoCtrl', ['$scope','$http', '$stateParams','$rootScope', '$modal', function($scope,$http, $stateParams,$rootScope,$modal) {
//       $scope.$parent.bname=$stateParams.bname;
//       $scope.bid=$stateParams.bid;
//       $http.get(appurl+'/bundle/getBundleInfo/'+$stateParams.bid).success( function(response) {
//         if(response.flag){
//           $scope.data=response;
//         }
//      });
//     }
//   ]);
//# sourceMappingURL=app.js.map

//http://localhost:8080/StatisticsApi/bundle/getDependent/org.jruby.jruby/230
fameControllers.controller('bundleDependentCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', function($scope, $http, $stateParams, $rootScope, $modal, $location) {
  $scope.$parent.bname = $stateParams.bname;
  $scope.$parent.bid = $stateParams.bid;
  showHideLoader('show');
  $http.get(appurl + '/bundle/getDependent/' + $stateParams.bname + "/" + $stateParams.bid).success(function(response) {
    var main = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    tab2.removeClass('active');
    var tab3 = angular.element(document.querySelector('#myTab li:nth-child(4)'));
    tab3.removeClass('active');
    var tab4 = angular.element(document.querySelector('#myTab li:nth-child(5)'));
    tab4.removeClass('active');
    var tab5 = angular.element(document.querySelector('#myTab li:nth-child(6)'));
    tab5.removeClass('active');
    showHideLoader('hide');
    $scope.tree = response.dependentBundleList;
    $.map($scope.tree, function(n) {
      return n.nodes = [];
    });

    //console.log(response.dependentBundleList==null)
    if (response.dependentBundleList == null) {
      $scope.errrormessage = response.message;

    } else {
      $scope.errrormessage = ""

    }

  });
  $scope.delete = function(data) {
    data.nodes = [];
  };

  $scope.createtree = function(datarow) {
    showHideLoader('show');
    $http.get(appurl + '/bundle/getDependent/' + datarow.bundleName + "/" + datarow.bundleId).success(function(response)

      {
        showHideLoader('hide');
        console.log(response.dependentBundleList);
        if (response.dependentBundleList == null) {

          $scope.add(response, datarow);

        } else {

          $scope.add(response, datarow);
        }
      });
    //
  }
  $scope.add = function(datatree, datarow) {
    console.log(datatree);

    if (datatree.dependentBundleList == null) {

      // datatree.dependentBundleList = [{
      //   bundleName: "Dependent not found"
      // }];

      $.map(datatree.dependentBundleList, function(n) {
        console.log(n);
        //nodes.length==1;
        return n.nodes = [];

      });
      //$scope.add(response,datarow);
      datarow.nodes = datatree.dependentBundleList;
    }
    //console.log(datatree.dependentBundleList);
    $.map(datatree.dependentBundleList, function(n) {
      console.log(n);
      return n.nodes = [];

    });
    datarow.nodes = datatree.dependentBundleList;

  };


}]);
// http://localhost:8080/StatisticsApi/bundle/cxf/com.cox.bis.customerupdateservices
fameControllers.controller('cxfCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$window', function($scope, $http, $stateParams, $rootScope, $modal, $location, $window) {
  $scope.$parent.bname = $stateParams.bname;
  $scope.$parent.bid = $stateParams.bid;

  showHideLoader('show');
  $http.get(appurl + '/bundle/cxf/' + $scope.bname).success(function(response)


    {
      //   response={
      // 	"message": null,
      // 	"flag": true,
      // 	"cxfDetialsResponse": [{
      // 		"endpoint": "/CustomerProfileManagement/CustomerCreditServices/v1/GetCustomerPPVCreditLimit",
      // 		"operations": ["getCreditLimitAmtByAccNum9"],
      // 		"url": "http://localhost:8181/cxf/CustomerProfileManagement/CustomerCreditServices/v1/GetCustomerPPVCreditLimit?wsdl",
      // 		"wsdl": true
      // 	}, {
      // 		"endpoint": "/CustomerProfileManagement/v1/CustomerCollectionStatsAndICCInfo",
      // 		"operations": ["getCustomerCollectionStats", "getCustomerICC"],
      // 		"url": "http://localhost:8181/cxf/CustomerProfileManagement/v1/CustomerCollectionStatsAndICCInfo?wsdl",
      // 		"wsdl": true
      // 	}, {
      // 		"endpoint": "/CustomerProfileManagement/CustomerCreditServices/v1/AssessFinancialRiskServices",
      // 		"operations": ["getCreditAndDeposit", "getPastDueWriteOff"],
      // 		"url": "http://localhost:8181/cxf/CustomerProfileManagement/CustomerCreditServices/v1/AssessFinancialRiskServices?wsdl",
      // 		"wsdl": true
      // 	}]
      // }
      var main = angular.element(document.querySelector('#myTab li:nth-child(6)'));
      main.addClass('active');
      var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
      tab1.removeClass('active');
      var tab2 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
      tab2.removeClass('active');
      var tab3 = angular.element(document.querySelector('#myTab li:nth-child(4)'));
      tab3.removeClass('active');
      var tab4 = angular.element(document.querySelector('#myTab li:nth-child(5)'));
      tab4.removeClass('active');
      var tab5 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
      tab5.removeClass('active');
      if (response.flag == true) {

        $scope.cxfDetials = response.cxfDetialsResponse;
        $scope.wsdl = function(url) {
          $window.open(url);
        };
        $scope.checkOpration = function(url, Opration) {
          showHideLoader('show');
          $http.post(appurl + '/bundle/buildsoaprequest?wsdlurl=' + url + '&opname=' + Opration).success(function(response) {
            var modalInstance = $modal.open({        
              animation: true,
                      templateUrl: 'partials/soap-request.html',
                      size: 'lg',
              scope: $scope,
              controller: function($scope, $modalInstance) {

                $scope.requestxml = response.requestXML;
                $scope.oldrequestxml = angular.copy($scope.requestxml);
                $scope.update = function() {
                  $scope.responsexml = $scope.request;
                }
                $scope.updatexml = function() {
                  showHideLoader('show');
                  $http({
                    method: 'POST',
                    url: appurl + '/bundle/soaprequest?wsdlurl=' + url + '&opname=' + Opration,
                    data: $scope.requestxml,
                  }).success(function(response) {
                    showHideLoader('hide');
                    if (response.flag == true) {
                      $scope.responsexml = response.responseXML;
                    } else if (response.flag == false) {
                      response.message;
                    }
                  }).finally(function() {
                    showHideLoader('hide');
                  });

                }
                $scope.resetxml = function() {
                  $scope.responsexml = null;
                }
                $scope.cancel = function() {
                  $modalInstance.close();
                }
              }      
            });

          }).finally(function() {
            showHideLoader('hide');
          })

        }
      } else if (response.flag == false) {
        $scope.cxfError = response.message;
      }

    }).finally(function() {
    showHideLoader('hide');
  })





}]);
fameControllers.controller('dsdetailCtrl', ['$scope', '$http', '$stateParams', '$modal', '$rootScope', 'growl', '$location', function($scope, $http, $stateParams, $modal, $rootScope, growl, $location) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];
  showHideLoader('show');
  $scope.isopen = [];
  $scope.isopen[0] = true;
  $scope.obj = {}

  $http.get(appurl + '/ds/list/').success(function(response) {
    //  response={"dataSouceNames":[{"type":"SQL_SERVER","names":["com.cox.bis.enterpriseservices:type=BasicDataSource","com.cox.bis.serviceability:type=BasicDataSource"]}],"flag":true,"message":null}
    if (response.flag) {
      $rootScope.dsName = response.dataSouceNames;
      $scope.default = $scope.dsName[0];
      //$scope.dstype=response.dataSouceNames.type;
      //console.log($scope.dataSouce.type);
      $scope.dsListError = null;
      if ($scope.dsName.length == 0) {
        $scope.dsListError = 'No record found';
      }
      showHideLoader('hide');
      //$scope.getDetails(response.dataSouceNames[0].names[0],0);
      console.log($scope.getDetails);
    } else {
      $scope.dsListError = response.message;
      showHideLoader('hide');
    }
  }).finally(function() {
    showHideLoader('hide');
  });

  $scope.getDetails = function(name, index) {

    $scope.obj.selected = index;
    $scope.obj.selectedname = name;

    if ($scope.tableshow && $scope.tableshow == name) {

      return;
    }

    showHideLoader('show');
    $scope.connectionAttr = {};
    $scope.tableshow = name
    return $http.get(appurl + '/ds/getDbInfo/' + name).success(function(response) {
      //response={"connectionAttr":{"MaxTotal":"100","NumActive":"0","NumIdle":"10","MaxIdle":"20","MinIdle":"10","UserName":"EnterpriseServices_User","Url":"jdbc:sqlserver://catl0db157:1500;instanceName=prodlike2;DatabaseName=EnterpriseServices;loginTimeout=5;socketTimeout=120"},"flag":true,"message":null}

      if (response.flag) {
        $scope.dbInfoError = null;

        $scope.connectionAttr = response.connectionAttr;

        if ($scope.connectionAttr.length == 0) {
          $scope.dbInfoError = 'No record found';
        }
        showHideLoader('hide');
      } else {
        $scope.dbInfoError = response.message;
        showHideLoader('hide');
      }

    }).finally(function() {
      showHideLoader('hide');
    });;

  }

  //http://localhost:8080/StatisticsApi/ds/getDSDependent/com.cox.bis.enterpriseservices:type=BasicDataSource
  var openModal = function(success) {
    var modalInstance = $modal.open({        
      animation: true,
              templateUrl: (!success) ? 'partials/popup-success.html' : 'partials/popup-error.html',
              size: 'sm',
      controller: function($scope, $modalInstance) {
        $scope.ok = function() {
          $modalInstance.close();
        }
        $scope.cancel = function() {
          $modalInstance.close();
        }
      }      
    });
  }
  $scope.dsDependent = function() {
    showHideLoader('show');
    return $http.get(appurl + '/ds/testConnection/' + $scope.obj.selectedname).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        //  console.log($scope.dstype);
        growl.addWarnMessage("Successfully Connected to " + response.url);
      } else if (response.flag == false) {
        growl.addErrorMessage("Failed to Connect Due to " + response.message);
      }
    });
  }

}]);
fameControllers.controller('logsCtrl', ['$scope', '$http', '$stateParams', '$modal', '$rootScope', 'growl', '$timeout', '$location', function($scope, $http, $stateParams, $modal, $rootScope, growl, $timeout, $location) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  if ($scope.activeTab == 'ActiveMQ') {
    $scope.countDetail = false;
  } else {
    $scope.countDetail = true;
  }
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];



  $scope.clearFilter = function() {

    showHideLoader('show');
    $scope.level = 'All';
    $scope.start = null;
    $scope.end = null;
    $scope.message = null;
    $http.get(appurl + '/log/getLogTail?url=' + $stateParams.ip + '&lineNo=50').success(function(response) {
      $scope.LogListError = false;

      $scope.LogListTable = null;
      if (response.flag) {
        $scope.LogListTable = response.logEvents;
        _.forEach($scope.triggerDetails, function(n) {
          n.startTime = new Date(n.startTime);
        });

        if ($scope.LogListTable.length == 0) {
          $scope.LogListError = 'No record found';
        }
      } else {
        $scope.LogListError = response.message;
        showHideLoader('hide');
      }

    }).finally(function() {
      showHideLoader('hide');
    });
  }
  $scope.clearFilter();
  $scope.viewLog = function(logEvent) {
    var modalInstance = $modal.open({
      templateUrl: 'partials/log-detail.html',
      //  scope: $scope,
      size: 'lg',
      controller: function($scope, $modalInstance) {
        $scope.logEvent = logEvent;
        $scope.logEvent.exception = _.uniq($scope.logEvent.exception);
        if ($scope.logEvent && $scope.logEvent.properties) {
          $scope.logEvent.properties["bundleName"] = $scope.logEvent.properties["bundle.name"];

          $scope.logEvent.properties["bundleId"] = $scope.logEvent.properties["bundle.id"];
          $scope.logEvent.properties["bundleVersion"] = $scope.logEvent.properties["bundle.version"];
          $scope.logEvent.properties["bundleCoordinates"] = $scope.logEvent.properties["maven.coordinates"];
        }
        $scope.ok = function() {
          $modalInstance.close();
        };

        $scope.cancel = function() {
          $modalInstance.dismiss('cancel');
        };

        $scope.delete = function() {
          $modalInstance.dismiss('cancel');
        };
      }


    })
  }


  $scope.start = '';
  $scope.end = '';
  //
  //  $scope.minStartDate = 0; //fixed date
  //  $scope.maxStartDate = $scope.end; //init value
  //  $scope.minEndDate = $scope.start; //init value
  //  $scope.maxEndDate = $scope.end; //fixed date same as $scope.maxStartDate init value

  //  $scope.$watch('start', function(v){
  //    $scope.minEndDate = v;
  //  });
  //  $scope.$watch('end', function(v){
  //    $scope.maxStartDate = v;
  //  });
  $scope.dtmax = new Date();
  $scope.openStart = function() {
    $timeout(function() {
      $scope.startOpened = true;
    });
  };

  $scope.openEnd = function() {
    $timeout(function() {
      $scope.endOpened = true;
    });
  };
  var validate = function() {
    if ($scope.start && $scope.end && $scope.start < $scope.end) {
      return false;

    } else {
      return true;
    }
  }
  $scope.dateOptions = {

  };
  $scope.filter = function() {
    //  if (angular.isDefined(LogListTable)) {
    //            LogListTable.fnDestroy();
    //          }
    if (!validate()) {

      return
    }
    var urlParam = "lineNo=1000";
    if ($scope.start) {
      urlParam += "&beforeTime=" + $scope.start.getTime();
    }
    //  else {
    //    urlParam+="beforeTime=0"
    //  }
    if ($scope.end) {
      urlParam += "&afterTime=" + $scope.end.getTime();
    }
    //  else {
    //    urlParam+="&afterTime=0"
    //  }
    if ($scope.message) {
      urlParam += "&matchesText=" + $scope.message;
    }
    //  else {
    //    urlParam+="&matchesText"
    //  }
    if ($scope.level) {
      urlParam += "&logLevel=" + $scope.level;
    } else {
      urlParam += "&logLevel=DEBUG"
    }
    showHideLoader('show');
    $http.get(appurl + "/log/filterLog?" + urlParam).success(function(response) {
      $scope.LogListError = false;
      //        response={
      //   "logEvents": [
      //     {
      //       "exception": null,
      //       "fileName": "?",
      //       "level": "ERROR",
      //       "logger": "org.eclipse.jetty.server.AbstractConnector",
      //       "methodName": "?",
      //       "className": "?",
      //       "thread": "pool-3-thread-1",
      //       "message": "Started SelectChannelConnector@0.0.0.0:8181",
      //       "containerName": "root",
      //       "host": null,
      //       "lineNumber": "?",
      //       "properties": {
      //         "bundle.version": "8.1.17.v20150415",
      //         //"bundle.name": "org.eclipse.jetty.aggregate.jetty-all-server",
      //         "bundle.id": "94",
      //         "maven.coordinates": "org.eclipse.jetty:jetty-ajp:8.1.17.v20150415 org.eclipse.jetty:jetty-annotations:8.1.17.v20150415 org.eclipse.jetty:jetty-client:8.1.17.v20150415 org.eclipse.jetty:jetty-continuation:8.1.17.v20150415 org.eclipse.jetty:jetty-deploy:8.1.17.v20150415 org.eclipse.jetty:jetty-http:8.1.17.v20150415 org.eclipse.jetty:jetty-io:8.1.17.v20150415 org.eclipse.jetty:jetty-jaspi:8.1.17.v20150415 org.eclipse.jetty:jetty-jmx:8.1.17.v20150415 org.eclipse.jetty:jetty-jndi:8.1.17.v20150415 org.eclipse.jetty:jetty-nested:8.1.17.v20150415 org.eclipse.jetty:jetty-plus:8.1.17.v20150415 org.eclipse.jetty:jetty-rewrite:8.1.17.v20150415 org.eclipse.jetty:jetty-security:8.1.17.v20150415 org.eclipse.jetty:jetty-server:8.1.17.v20150415 org.eclipse.jetty:jetty-servlet:8.1.17.v20150415 org.eclipse.jetty:jetty-servlets:8.1.17.v20150415 org.eclipse.jetty:jetty-util:8.1.17.v20150415 org.eclipse.jetty:jetty-webapp:8.1.17.v20150415 org.eclipse.jetty:jetty-websocket:8.1.17.v20150415 org.eclipse.jetty:jetty-xml:8.1.17.v20150415 org.eclipse.jetty.aggregate:jetty-all-server:8.1.17.v20150415"
      //       },
      //       "seq": null,
      //       "timestamp": 1461573993000
      //     },
      //     {
      //       "exception": null,
      //       "fileName": "?",
      //       "level": "ERROR",
      //       "logger": "org.ops4j.pax.web.service.jetty.internal.JettyServerImpl",
      //       "methodName": "?",
      //       "className": "?",
      //       "thread": "pool-3-thread-1",
      //       "message": "Pax Web available at [0.0.0.0]:[8181]",
      //       "containerName": "root",
      //       "host": null,
      //       "lineNumber": "?",
      //       "properties": {
      //         "bundle.version": "3.2.3",
      //       //  "bundle.name": "org.ops4j.pax.web.pax-web-jetty",
      //         "bundle.id": "101",
      //         "maven.coordinates": "org.ops4j.base:ops4j-base-lang:1.5.0 org.ops4j.base:ops4j-base-util-collections:1.5.0 org.ops4j.base:ops4j-base-util-xml:1.5.0 org.ops4j.pax.web:pax-web-jetty:3.2.3"
      //       },
      //       "seq": null,
      //       "timestamp": 1461573993000
      //     },
      //     {
      //       "exception": null,
      //       "fileName": "?",
      //       "level": "ERROR",
      //       "logger": "org.springframework.osgi.extender.internal.support.ExtenderConfiguration",
      //       "methodName": "?",
      //       "className": "?",
      //       "thread": "FelixStartLevel",
      //       "message": "No custom extender configuration detected; using defaults...",
      //       "containerName": "root",
      //       "host": null,
      //       "lineNumber": "?",
      //       "properties": {
      //         "bundle.version": "1.2.1",
      //         "bundle.name": "org.springframework.osgi.extender",
      //         "bundle.id": "174",
      //         "maven.coordinates": "org.springframework.osgi:spring-osgi-extender:1.2.1"
      //       },
      //       "seq": null,
      //       "timestamp": 1461573993000
      //     },
      //     {
      //       "exception": null,
      //       "fileName": "?",
      //       "level": "ERROR",
      //       "logger": "org.apache.karaf.management.mbeans.scr.internal.ScrServiceMBeanImpl",
      //       "methodName": "?",
      //       "className": "?",
      //       "thread": "Blueprint Extender: 3",
      //       "message": "Activating the Apache Karaf SCR Service MBean",
      //       "containerName": "root",
      //       "host": null,
      //       "lineNumber": "?",
      //       "properties": {
      //         "bundle.version": "2.4.0.redhat-620133",
      //         "bundle.name": "org.apache.karaf.management.mbeans.scr",
      //         "bundle.id": "159",
      //         "maven.coordinates": "org.apache.karaf.management.mbeans:org.apache.karaf.management.mbeans.scr:2.4.0.redhat-620133"
      //       },
      //       "seq": null,
      //       "timestamp": 1461573993000
      //     },
      //     {
      //       "exception": null,
      //       "fileName": "?",
      //       "level": "ERROR",
      //       "logger": "org.springframework.scheduling.timer.TimerTaskExecutor",
      //       "methodName": "?",
      //       "className": "?",
      //       "thread": "FelixStartLevel",
      //       "message": "Initializing Timer",
      //       "containerName": "root",
      //       "host": null,
      //       "lineNumber": "?",
      //       "properties": {
      //         "bundle.version": "3.2.12.RELEASE_1",
      //         "bundle.name": "org.apache.servicemix.bundles.spring-context",
      //         "bundle.id": "169",
      //         "maven.coordinates": "org.apache.servicemix.bundles:org.apache.servicemix.bundles.spring-context:3.2.12.RELEASE_1"
      //       },
      //       "seq": null,
      //       "timestamp": 1461573993000
      //     }
      //   ],
      //   "flag": true,
      //   "message": null
      // }
      $scope.LogListTable = null;
      if (response.flag) {
        $scope.LogListTable = response.logEvents;
        _.forEach($scope.triggerDetails, function(n) {
          n.startTime = new Date(n.startTime);
        });
        if ($scope.LogListTable.length == 0) {
          $scope.LogListError = 'No record found';
        }
      } else {
        $scope.LogListError = response.message;
        showHideLoader('hide');
      }

    }).finally(function() {
      showHideLoader('hide');
    });

  }

}]);
fameControllers.controller('amqCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  if ($scope.activeTab == 'ActiveMQ') {
    $scope.countDetail = false;
  } else {
    $scope.countDetail = true;
  }
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];

  $scope.go = function(type) {
    console.log(type);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/" + $stateParams.ip + "/Amq/" + type);
  }
  var calPercent = function(used, max) {
    return parseInt((used / max) * 100)
  }

  function addZero(i) {
    if (i < 10) {
      i = "0" + i;
    }
    return i;
  }

  function formatDate() {
    var timeByType = new Date();
    var d = addZero(timeByType.getDate());
    var month = new Array();
    month[0] = "January";
    month[1] = "February";
    month[2] = "March";
    month[3] = "April";
    month[4] = "May";
    month[5] = "June";
    month[6] = "July";
    month[7] = "August";
    month[8] = "September";
    month[9] = "October";
    month[10] = "November";
    month[11] = "December";
    var mon = month[timeByType.getMonth()];
    var y = addZero(timeByType.getFullYear());
    var h = addZero(timeByType.getHours());
    var m = addZero(timeByType.getMinutes());
    var s = addZero(timeByType.getSeconds());
    return (h + ":" + m + ":" + s);

  }

  $scope.createChart = function(response) {
    var timeByType = formatDate()
    $scope.chart = AmCharts.makeChart("chartdiv", {
      "type": "serial",
      "theme": "light",
      "legend": {
        "useGraphSettings": true
      },
      "dataProvider": [{
        "period": timeByType,
        "Heap": calPercent(response.heapMemory.used, response.heapMemory.committed),
        "Non Heap": calPercent(response.nonHeapMemory.used, response.nonHeapMemory.committed)
      }],
      "valueAxes": [{
        "integersOnly": true,
        "axisAlpha": 0,
        "dashLength": 5,
        "gridCount": 10,
        "position": "left",
        "title": "Place taken"
      }],
      "startDuration": 0.5,
      "graphs": [{
        "balloonText": "<b>Heap: [[value]]</b>",
        "fillColorsField": "color",
        "fillAlphas": 0.9,
        "lineAlpha": 0.2,
        "type": "column",
        "valueField": "Heap"
      }, {
        "balloonText": "<b>Non Heap: [[value]]</b>",
        "fillColorsField": "color",
        "fillAlphas": 0.9,
        "lineAlpha": 0.2,
        "type": "column",
        "valueField": "Non Heap"
      }],
      "chartCursor": {
        "cursorAlpha": 0,
        "zoomable": false
      },
      "categoryField": "period",
      "categoryAxis": {
        "gridPosition": "start",
        "axisAlpha": 0,
        "fillAlpha": 0.05,
        "fillColor": "#000000",
        "gridAlpha": 0,
        "position": "top"
      },
      "export": {
        "enabled": true,
        "position": "bottom-right"
      }
    });

  }

  var callCart = function() {
    showHideLoader('show');
    return $http.get(appurl + '/memory/allMemory/' + $scope.ip).success(function(response) {
      showHideLoader('hide');
      $scope.createChart(response);

    });
  }
  callCart();
}]);
fameControllers.controller('homeCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  var main = angular.element(document.querySelector('#myTab li:nth-child(1)'));
  main.addClass('active');
  var tab1 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
  tab1.removeClass('active');
  var tab2 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
  tab2.removeClass('active');
  showHideLoader('show');
  $http.get(appurl + '/amq/home/' + $scope.ip).success(function(response) {


    showHideLoader('hide');
    if (response.flag == true) {
      $scope.amqInfo = response.amqBrokerInfo;
    } else {
      $scope.amqError = response.message
    }
  });
}]);
fameControllers.controller('queueCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', "Queuelist", "growl","$window", function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, Queuelist, growl,$window) {

  $scope.addQueue = function() {
    var modalInstance = $modal.open({        
      animation: true,
      templateUrl: 'partials/addQueue.html',
      size: 'md',
      controller: function($scope, $modalInstance) {
        $scope.addRow = function() {
          $modalInstance.close({
            name: $scope.name
          });
        };
        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      }      
    });
    modalInstance.result.then(function(data) {
      $http.post(appurl + '/amq/add?name=' + data.name + '&brokerName=' + Queuelist.data.amqBrokerInfo.objName + '&isQueue=' + true).success(function(response) {
        console.log(response);
        if (response.flag == true) {
          growl.addWarnMessage(response.message);
          $window.location.reload();
          $scope.QueueData.unshift(data);


        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    });

  }

  $scope.purgeQueue = function(index) {

    showHideLoader('show');
    $http.post(appurl + '/amq/purgeQueue?queueObjName=' + $scope.QueueData[index].objName).success(function(response) {
      if (response.flag == true) {
        growl.addWarnMessage(response.message);
        $window.location.reload();
      } else if (response.flag == false) {
        growl.addErrorMessage(response.message);
      }
    }).finally(function() {
      showHideLoader('hide');
    })
  }
  $rootScope.removeQueue = function(name, data) {
    $scope.Queuename = $scope.QueueData[name].Name;
    showHideLoader('show');
    $http.post(appurl + "/amq/remove?name=" + $scope.Queuename + "&brokerName=" + Queuelist.data.amqBrokerInfo.objName + "&isQueue=" + true).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        $scope.QueueData.splice(data);
        growl.addWarnMessage(response.message);
        $window.location.reload();
      } else if (response.flag == false) {
        growl.addErrorMessage(response.message);
      }
    });
  }
  $scope.deleteQueue = function(data) {
    console.log(data);
    $scope.modalInstance = $modal.open({
      templateUrl: 'partials/deleteQueue.html',
      size: 'md',
      scope: $scope,
      controller: function($scope, $rootScope, $modalInstance) {

        $scope.delefunction = function() {
          $rootScope.removeQueue(data);
          $modalInstance.dismiss('cancel');
        }
        $scope.ok = function() {
          $modalInstance.close();
        };

        $scope.cancel = function() {
          $modalInstance.dismiss('cancel');
        };

        $scope.delete = function() {

          $modalInstance.dismiss('cancel');
        };
      }


    })

  }

  $scope.sendQueue = function(index) {
    var modalInstance = $modal.open({        
      animation: true,
      templateUrl: 'partials/sendQueue.html',
      size: 'md',
      scope: $scope,
      controller: function($scope, $modalInstance) {
        $scope.destination=$scope.QueueData[index].Name;
        $scope.noMessage=1;
        $scope.sendTo = function() {
          $modalInstance.close({
            JMSDestination : $scope.destination,
            JMSReplyTo:$scope.replyTo,
            JMSCorrelationID:$scope.correlation,
            JMSDeliveryMode:$scope.persistent,
            JMSMessageCount:$scope.noMessage,
            messageBody:$scope.messageBody,
          });
        };
        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      }      
    });
    modalInstance.result.then(function(data, postData) {
      $scope.messageData=data.messageBody
      delete data.messageBody


    //  http://localhost:8091/StatisticsApi/amq/sendMsg?objName=org.apache.activemq:brokerName=broker-1,destinationName=test,destinationType=Queue,type=Broker&message=testing queue
    showHideLoader('show');
    return $http({
      method: 'POST',
      url: appurl + '/amq/sendMsg?objName=' + $scope.QueueData[index].objName + '&message=' + $scope.messageData,
      data :data
    }).success(function(response) {
      showHideLoader('hide');
        if (response.flag == true) {
          growl.addWarnMessage(response.message);
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      }).finally(function(){
        showHideLoader('hide');
      })
    });

  }
  var intervelTable = null;
  var QueueTable=function(showLoader){
  var postData = Queuelist.data.amqBrokerInfo.Queues;
  if(showLoader) {
    showHideLoader('show');
  }
  return $http({
    method: 'POST',
    url: appurl + "/amq/list",
    data: postData
  }).success(function(response) {
    var main = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    tab2.removeClass('active');
    $scope.QueueData = response.destinationDetailsList;
  }).finally(function() {
    showHideLoader('hide');
  });
}
  QueueTable(true);
    intervelTable = setInterval(function() {
      QueueTable();
    }, 10000);
    $scope.$on("$destroy", function() {
      clearInterval(intervelTable)
    });
}]);
fameControllers.controller('topicCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', 'Queuelist','growl',"$window", function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, Queuelist,growl,$window) {

  $scope.addTopic = function() {
    var modalInstance = $modal.open({        
      animation: true,
      templateUrl: 'partials/addTopic.html',
      size: 'md',
      controller: function($scope, $modalInstance) {
        $scope.addRow = function() {
          $modalInstance.close({
            name: $scope.name
          });
        };
        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      }      
    });
    modalInstance.result.then(function(data) {
      $http.post(appurl + '/amq/add?name=' + data.name + '&brokerName=' + Queuelist.data.amqBrokerInfo.objName + '&isQueue=' + false).success(function(response) {
        console.log(response);
        if (response.flag == true) {
          $scope.TopicData.unshift(data);
          $window.location.reload();
          growl.addWarnMessage(response.message);
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    });

  }
  $rootScope.removeTopic = function(name, data) {
    $scope.Queuename = $scope.TopicData[name].Name;
    showHideLoader('show');
    $http.post(appurl + "/amq/remove?name=" + $scope.Queuename + "&brokerName=" + Queuelist.data.amqBrokerInfo.objName + "&isQueue=" + false).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        $scope.TopicData.splice(data);
        growl.addWarnMessage(response.message);
        $window.location.reload();
      } else if (response.flag == false) {
        growl.addErrorMessage(response.message);
      }
    });
  }
  $scope.deleteTopic = function(data) {
    console.log(data);
    $scope.modalInstance = $modal.open({
      templateUrl: 'partials/deleteTopic.html',
      size: 'md',
      scope: $scope,
      controller: function($scope, $rootScope, $modalInstance) {

        $scope.delefunction = function() {
          console.log(data);
          $rootScope.removeTopic(data);
          $modalInstance.dismiss('cancel');
        }
        $scope.ok = function() {
          $modalInstance.close();
        };

        $scope.cancel = function() {
          $modalInstance.dismiss('cancel');
        };

        $scope.delete = function() {

          $modalInstance.dismiss('cancel');
        };
      }


    })

  }
  $scope.sendQueue = function(index) {
    var modalInstance = $modal.open({        
      animation: true,
      templateUrl: 'partials/sendQueue.html',
      size: 'md',
      scope: $scope,
      controller: function($scope, $modalInstance) {
        $scope.destination=$scope.TopicData[index].Name;
        $scope.noMessage=1;
        $scope.sendTo = function() {
          $modalInstance.close({
            JMSDestination : $scope.destination,
            JMSReplyTo:$scope.replyTo,
            JMSCorrelationID:$scope.correlation,
            JMSDeliveryMode:$scope.persistent,
            JMSMessageCount:$scope.noMessage,
            messageBody:$scope.messageBody,
          });
        };
        $scope.cancel = function() {
          $modalInstance.dismiss();
        }
      }      
    });
    modalInstance.result.then(function(data, postData) {
      $scope.messageData=data.messageBody
      delete data.messageBody


    //  http://localhost:8091/StatisticsApi/amq/sendMsg?objName=org.apache.activemq:brokerName=broker-1,destinationName=test,destinationType=Queue,type=Broker&message=testing queue
    showHideLoader('show');
    return $http({
      method: 'POST',
      url: appurl + '/amq/sendMsg?objName=' + $scope.TopicData[index].objName + '&message=' + $scope.messageData,
      data :data
    }).success(function(response) {
      showHideLoader('hide');
        if (response.flag == true) {
          growl.addWarnMessage(response.message);
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      }).finally(function(){
        showHideLoader('hide');
      })
    });

  }
  var intervelTable = null;
  var TopicTable=function(showLoader){
  var postData = Queuelist.data.amqBrokerInfo.Topics;
  if(showLoader){
    showHideLoader('show');
  }
  return $http({
    method: 'POST',
    url: appurl + "/amq/list",
    data: postData
  }).success(function(response) {
    var main = angular.element(document.querySelector('#myTab li:nth-child(3)'));
    main.addClass('active');
    var tab1 = angular.element(document.querySelector('#myTab li:nth-child(1)'));
    tab1.removeClass('active');
    var tab2 = angular.element(document.querySelector('#myTab li:nth-child(2)'));
    tab2.removeClass('active');
    showHideLoader('hide');
    $scope.TopicData = response.destinationDetailsList;
  });
}
  TopicTable(true);
    intervelTable = setInterval(function() {
      TopicTable();
    }, 10000);
    $scope.$on("$destroy", function() {
      clearInterval(intervelTable)
    });
}]);
fameControllers.controller('MsgCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', 'growl',"$window", function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, growl,$window) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.objName = $stateParams.objName;
  $scope.msgTable=null;
  if ($scope.activeTab == 'ActiveMQ') {
    $scope.countDetail = false;
  } else {
    $scope.countDetail = true;
  }
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];
//  http://localhost:8091/StatisticsApi/amq/browse?objName=org.apache.activemq:brokerName=broker-1,destinationName=test,destinationType=Queue,type=Broker
showHideLoader('show');
$http.get(appurl + '/amq/browse?objName=' + $scope.objName).success(function(response) {
  if (response.flag == true) {
    $scope.msgTable=response.messageList;
    if ($scope.msgTable.length == 0) {
      $scope.MsgError=response.message;
    }
  } else {
    $scope.MsgError=response.message;
  }
}).finally (function(){
  showHideLoader('hide');
})
$rootScope.removeMsg = function(name, data) {
  $scope.Message = $scope.msgTable[name].JMSMessageID;
  showHideLoader('show');
  $http.post(appurl + "/amq/removeMsg?objName=" + $scope.objName + "&msgId=" +  $scope.Message).success(function(response) {
    showHideLoader('hide');
    if (response.flag == true) {
      $scope.msgTable.splice(data);
      growl.addWarnMessage(response.message);
      $window.location.reload();
    } else if (response.flag == false) {
      growl.addErrorMessage(response.message);
    }
  });
}
$scope.deleteMsg = function(data) {
  console.log(data);
  $scope.modalInstance = $modal.open({
    templateUrl: 'partials/deleteMsg.html',
    size: 'md',
    scope: $scope,
    controller: function($scope, $rootScope, $modalInstance) {

      $scope.delefunction = function() {
        console.log(data);
        $rootScope.removeMsg(data);
        $modalInstance.dismiss('cancel');
      }
      $scope.ok = function() {
        $modalInstance.close();
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.delete = function() {

        $modalInstance.dismiss('cancel');
      };
    }
  })
}
$scope.msgDetail = function(msgId) {
	//console.log("got msgId >>> "+msgId)
  $scope.modalInstance = $modal.open({
    templateUrl: 'partials/msgDetail.html',
    size: 'lg',
    scope: $scope,
    controller: function($scope, $rootScope, $modalInstance) {
//http://localhost:8091/StatisticsApi/amq/browseMsg?objName=org.apache.activemq:brokerName=broker-1,destinationName=test,destinationType=Queue,type=Broker&msgId=ID:XEN-945-53229-1473762731992-4:1:1:1:1
showHideLoader('show');
$http.get(appurl + '/amq/browseMsg?objName=' + $scope.objName + '&msgId='+ msgId).success(function(response) {
  if(response.flag==true){
    $scope.MsgDetails=response.messageDetail;
  }

}).finally (function(){
  showHideLoader('hide');
})
      $scope.ok = function() {
        $modalInstance.close();
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.delete = function() {

        $modalInstance.dismiss('cancel');
      };
    }
  })
}
}]);
fameControllers.controller('ConsumerCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', 'growl',"$window", function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, growl,$window) {
  $scope.ip = $stateParams.ip;
  $scope.evnid = $stateParams.evnid;
  $scope.activeTab = $stateParams.activeTab;
  $scope.Subscription = $stateParams.Subscription;
  $scope.msgTable=null;
  if ($scope.activeTab == 'ActiveMQ') {
    $scope.countDetail = false;
  } else {
    $scope.countDetail = true;
  }
  $scope.gotoServer = function(server) {
    console.log('evnid' + $stateParams.evnid);
    $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
  }
  $scope.severDetails = _.where($rootScope.serverlist, {
    "name": "" + $stateParams.ip
  })[0];

    showHideLoader('show');
    var postData=$scope.Subscription;
    console.log(postData);
    return $http({
      method: 'POST',
      url: appurl + '/amq/consumers',
      data:postData,
    }).success(function(response) {
      showHideLoader('hide');
        if (response.flag == true) {
          $scope.consumerTable=response.consumers;
          if($scope.consumerTable.length==0){
            $scope.MsgError=("No Consumer Found!");
          }
        }else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }

      }).finally(function(){
        showHideLoader('hide');
      })
  }]);
  fameControllers.controller('SubscriberCtrl', ['$scope', '$http', '$stateParams', '$rootScope', '$modal', '$location', '$timeout', 'growl',"$window", function($scope, $http, $stateParams, $rootScope, $modal, $location, $timeout, growl,$window) {

    $scope.ip = $stateParams.ip;
    $scope.evnid = $stateParams.evnid;
    $scope.activeTab = $stateParams.activeTab;
      $scope.objName = $stateParams.objName;
    $scope.TopicName = $stateParams.topicName;
    $scope.MsgError=null;
    if ($scope.activeTab == 'ActiveMQ') {
      $scope.countDetail = false;
    } else {
      $scope.countDetail = true;
    }


    $scope.gotoServer = function(server) {
      console.log('evnid' + $stateParams.evnid);
      $location.path("/app/" + $scope.activeTab + "/" + $stateParams.evnid + "/serverdashboard/" + server);
    }
    $scope.severDetails = _.where($rootScope.serverlist, {
      "name": "" + $stateParams.ip
    })[0];
    showHideLoader('show');
    $http.get(appurl + '/amq/home/' + $scope.ip).success(function(response) {
      showHideLoader('hide');
      if (response.flag == true) {
        $scope.amqInfo = response.amqBrokerInfo;
      }
    });
    $scope.addSubscriber = function() {
      console.log($scope.TopicName);
      var modalInstance = $modal.open({        
        animation: true,
        templateUrl: 'partials/addSubscriber.html',
        size: 'md',
        scope: $scope,
        controller: function($scope, $modalInstance) {
          $scope.sendTo = function() {
            $modalInstance.close({
              client : $scope.ClientId,
              subscriber:$scope.SubscriberName,
              topic:$scope.TopicName,
              selector:$scope.Selector
            });
          };
          $scope.cancel = function() {
            $modalInstance.dismiss();
          }
        }      
      });
      modalInstance.result.then(function(data) {
      //  http://localhost:8091/StatisticsApi/amq/sendMsg?objName=org.apache.activemq:brokerName=broker-1,destinationName=test,destinationType=Queue,type=Broker&message=testing queue
      showHideLoader('show');
      return $http({
        method: 'POST',
        url: appurl + '/amq/subscriber/add?amqObjName=' + $scope.amqInfo.objName + '&client=' + data.client + '&subscriberName=' + data.subscriber + '&topicName=' + data.topic + '&selector=' + data.selector
      }).success(function(response) {
        showHideLoader('hide');
          if (response.flag == true) {
            $scope.MsgError=null;
            console.log($scope.msgTable);
            growl.addWarnMessage(response.message);
            $scope.subscribersTable=response.subscriber;
          } else if (response.flag == false) {
            growl.addErrorMessage(response.message);
          }
        });
      });

    }

    $rootScope.removeMsg = function(name, data) {
      $scope.clientId =$scope.subscribersTable[name].clientId ;
      $scope.subscriberName =$scope.subscribersTable[name].subscriptionName ;
    //  $scope.objectName =$scope.subscribersTable[name].objectName;
      showHideLoader('show');
    // http://localhost:8080/StatisticsApi/amq/subscriber/remove?amqObjName=org.apache.activemq:type=Broker,brokerName=brokerN&client=Nikita&subscriberName=Subscriber-Nikita&topicName=External_Topic
      $http.post(appurl + "/amq/subscriber/remove?amqObjName=" + $scope.amqInfo.objName + "&client=" +  $scope.clientId + "&subscriberName=" + $scope.subscriberName + "&topicName="+$scope.TopicName).success(function(response) {
        showHideLoader('hide');
        if (response.flag == true) {
          $scope.subscribersTable.splice(data, 1);
          growl.addWarnMessage("subscriber Deleted Successfully");
          $scope.subscribersTable=response.subscriber;
          if($scope.subscribersTable.length==0){
            $scope.MsgError="No subscriber Found!!!";
          }
        } else if (response.flag == false) {
          growl.addErrorMessage(response.message);
        }
      });
    }
    $scope.deleteSubscriber = function(data) {
      console.log(data);
      $scope.modalInstance = $modal.open({
        templateUrl: 'partials/deleteSub.html',
        size: 'md',
        scope: $scope,
        controller: function($scope, $rootScope, $modalInstance) {

          $scope.delefunction = function() {
            console.log(data);
            $rootScope.removeMsg(data);
            $modalInstance.dismiss('cancel');
          }
          $scope.ok = function() {
            $modalInstance.close();
          };

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          };

          $scope.delete = function() {

            $modalInstance.dismiss('cancel');
          };
        }
      })
    }
      showHideLoader('show');
      return $http({
        method: 'POST',
    //    http://localhost:8080/StatisticsApi/amq/subscribers?topicObjName=org.apache.activemq:brokerName=brokerN,destinationName=A,destinationType=Topic,type=Broker
        url: appurl + '/amq/subscribers?topicObjName='+ $scope.objName
      }).success(function(response) {
        showHideLoader('hide');
          if (response.flag == true) {
            $scope.subscribersTable=response.subscriber;
            if($scope.subscribersTable.length==0){
              $scope.MsgError=("No Consumer Found!");
            }
          }else if (response.flag == false) {
            growl.addErrorMessage(response.message);
          }

        }).finally(function(){
          showHideLoader('hide');
        })

    }]);
