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

    $scope.addSlide('https://raw.github.com/hemantasapkota/sloot-editor/master/screenshots/s2/SlootEditorEntity.png', 'Sloot Editor');
    $scope.addSlide('https://raw.github.com/hemantasapkota/sloot-editor/master/screenshots/s2/SlootEditorRender.png', 'Render View');
    $scope.addSlide('https://raw.github.com/hemantasapkota/sloot-editor/master/screenshots/s2/SlootEditorEntity.png', 'Entity Editor');
    $scope.addSlide('https://raw.github.com/hemantasapkota/sloot-editor/master/screenshots/s2/SlootEditorScripting.png', 'Scripting');

  });
