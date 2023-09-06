


(function ($) {
  "use strict";
  
$(document).ready(function() {
    $('#sendOtpBtn').click(function(e) {
        e.preventDefault(); // prevent the default form submission behavior
        $.ajax({
            type: "POST",
            url: "/send-otp",
            data: $('#forgetPasswordModal form:first').serialize(), // serialize the first form data inside the modal
            success: function() {
                // handle the successful form submission
            },
            error: function() {
                // handle the form submission error
            }
        });
    });
});
 $(document).ready(function() {
          // Disable the confirm button by default
          $("#confirmBtn").prop("disabled", true);

          // Listen for changes to the password and confirm password fields
          $("input[name='newPassword'], input[name='confirmPassword']").on("input", function() {
            var password = $("input[name='newPassword']").val();
            var confirmPassword = $("input[name='confirmPassword']").val();

            // If the passwords match, enable the confirm button
            if (password === confirmPassword) {
              $("#confirmBtn").prop("disabled", false);
            } else {
              // Otherwise, disable the confirm button
              $("#confirmBtn").prop("disabled", true);
            }
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
