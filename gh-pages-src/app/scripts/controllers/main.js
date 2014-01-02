'use strict';

angular.module('ghPagesSrcApp')
  .controller('MainCtrl', function ($scope) {

    $scope.myInterval = 5000;
    var slides = $scope.slides = [];

    $scope.addSlide = function(img, desc) {

      slides.push({
        image: img,
        text: desc
      });

    };

    $scope.addSlide('https://raw.github.com/hemantasapkota/sloot-editor/master/screenshots/EdLuaScript.png', 'Sloot Editor');
    $scope.addSlide('', 'Sloot Editor');
    $scope.addSlide('', 'Sloot Editor');
    $scope.addSlide('', 'Sloot Editor');

  });
