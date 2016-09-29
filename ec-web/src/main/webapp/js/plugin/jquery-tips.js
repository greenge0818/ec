define("jquery-tips",["jquery"], function (require, exports, module) {
    var $ = require("jquery");
    $.fn.tips = function(opts) {
    var settings = $.extend({
      speed: 150,
      position: opts
    }, opts);

    return this.each(function() {
      var $this = $(this);

      var tip = $this.attr('data-tip') || $this.attr('title');
      var pos = $this.attr('data-position');
      if (!pos) {
        pos = settings.position;
      }

      $this.hover(function() {
        var $container = $("<div class='tips-container tips-container-" + pos + "'>");
        $container.append($("<div class='tips-caret-" + pos + "'>"));
        $container.append($("<div class='tips-content' >").append(tip));

        $this.removeAttr('title');

        $container.appendTo($('body'));

        var tipWidth = $container.outerWidth();
        var elWidth  = $this.width();

        var elX = $this.offset().left;
		var pts = $this.parents(),top = 0;
		$.each(pts, function() {
			if($(this).css("position") == "relative" || $(this).css("position") == "absolute" ){
				top +=$(this).position().top;
			}
			
		});
        var elBottom = top+$this.position().top + $this.outerHeight(), elLeft = $this.position().left;
        if (pos === "top") {
          elBottom -= $this.outerHeight() + $container.outerHeight() + 13;
          $container.css('left', elX - (tipWidth - elWidth) / 2)
                    .css('top', elBottom)
                    .fadeIn(settings.speed);
        }else if (pos === "right") {

            elBottom -= $container.outerHeight();
            $container.css('left', elX + elWidth + 2)
                      .css('top', elBottom-2)
                      .fadeIn(settings.speed);

        }else if (pos === "left") {

            elBottom -= $container.outerHeight()-10;
            $container.css('left', elX - (tipWidth + elWidth + 30)/2)
                      .css('top', elBottom)
                      .fadeIn(settings.speed);
        } else if (pos === "bottom") {
            elBottom -= $this.outerHeight() + $container.outerHeight() - 45;
            $container.css('left', elX - (tipWidth - elWidth) / elX)
                    .css('top', elBottom)
                    .fadeIn(settings.speed);
        }


      }, function() {
        	　$(".tips-container").fadeOut(settings.speed).delay().remove();
      });
    });
  }


})


