	myApp.controller("cao_controller",function($scope,$timeout,popService)
	{
		
		$scope.popover = myApp.rootScope.popover;
		popService.setPopover($scope.popover);
		
    $scope.show = function(e) {
    	$scope.popover.show(e);
    };
    
  	$scope.hide = function()
  	{
  		console.log("hide popover");
  		$scope.popover.hide();
  	};
		$scope.add_a_device=function(e)
		{
			$scope.show(e);
			
		};
	}); 