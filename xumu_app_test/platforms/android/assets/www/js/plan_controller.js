	myApp.controller("plan_controller",function($scope,$timeout,popService)
	{
		$scope.popover = myApp.rootScope.popoverPln;
		popService.setPopoverPln($scope.popover);
		
    $scope.show = function(e) {
    	$scope.popover.show(e);
    };
    
  	$scope.hide = function()
  	{
  		console.log("hide popover");
  		$scope.popover.hide();
  	};
		$scope.add_plan=function(e)
		{
			$scope.show(e);
			
		};
	});