


(function ($) {
  "use strict";
  
$(document).ready(function() {
    $('#sendOtpBtn').click(function(e) {
        e.preventDefault(); // prevent the default form submission behavior
        $.ajax({
            type: "POST",
            url: "/send-otp",
            data: $('#forgetPasswordModal form').serialize(), // serialize the form data
            success: function() {
                // handle the successful form submission
            },
            error: function() {
                // handle the form submission error
            }
        });
    });
});

  // Spinner
  var spinner = function () {
    setTimeout(function () {
      if ($("#spinner").length > 0) {
        $("#spinner").removeClass("show");
      }
    }, 1);
  };
  spinner();

  // Back to top button
  $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
      $(".back-to-top").fadeIn("slow");
    } else {
      $(".back-to-top").fadeOut("slow");
    }
  });
  $(".back-to-top").click(function () {
    $("html, body").animate({ scrollTop: 0 }, 1500, "easeInOutExpo");
    return false;
  });

  // Sidebar Toggler
  $(".sidebar-toggler").click(function () {
    $(".sidebar, .content").toggleClass("open");
    return false;
  });

  // Progress Bar
//  $(".pg-bar").waypoint(
//    function () {
//      $(".progress .progress-bar").each(function () {
//      $(this).css("width", $(this).attr("aria-valuenow") + "%");
//  });
//    },
//    { offset: "80%" }
//  );

  // Calender
})(jQuery);

//mail on/off btn in Employee Dashboard Screen
const toggle = document.querySelector("#mail-toggle");
toggle.addEventListener(() => {
  const onOff = toggle.parentNode.querySelector(".changes");
  onOff.innerHTML = toggle.checked
    ? '<i class="fa-solid fa-bell"></i>Mail '
    : '<i class="fa-solid fa-bell"></i>Email';
});
