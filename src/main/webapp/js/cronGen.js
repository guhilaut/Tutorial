

(function ($) {

    var resultsName = "";
    var inputElement;
    var displayElement;

    $.fn.extend({
        cronGen: function () {

            //create top menu
            var cronContainer = $("<div/>", { id: "CronContainer"});
            var mainDiv = $("<div/>", { id: "CronGenMainDiv" });
            var topMenu = $("<ul/>", { "class": "nav nav-tabs", id: "CronGenTabs" });
            $('<li/>', { 'class': 'active' }).html($('<a id="MinutesTab" href="#Minutes">Minutes</a>')).appendTo(topMenu);
            $('<li/>').html($('<a id="HourlyTab" href="#Hourly">Hourly</a>')).appendTo(topMenu);
            $('<li/>').html($('<a id="DailyTab" href="#Daily">Daily</a>')).appendTo(topMenu);
            $('<li/>').html($('<a id="WeeklyTab" href="#Weekly">Weekly</a>')).appendTo(topMenu);
            $('<li/>').html($('<a id="MonthlyTab" href="#Monthly">Monthly</a>')).appendTo(topMenu);
            $('<li/>').html($('<a id="YearlyTab" href="#Yearly">Yearly</a>')).appendTo(topMenu);
            $(topMenu).appendTo(mainDiv);

            //create what's inside the tabs
            var container = $("<div/>", { "class": "container-fluid", "style": "margin-top: 10px" });
            var row = $("<div/>", { "class": "row" });
            var span12 = $("<div/>", { "class": "span12" });
            var tabContent = $("<div/>", { "class": "tab-content", "style": "margin-top: -10px" });

            //creating the minutesTab
            var minutesTab = $("<div/>", { "class": "tab-pane active", id: "Minutes" });
            $(minutesTab).append("Every&nbsp;");
            $("<input/>", { id: "MinutesInput", type: "text", value: "1", style: "width: 40px" }).appendTo(minutesTab);
            $(minutesTab).append("&nbsp;minute(s)");
            $(minutesTab).appendTo(tabContent);

            //creating the hourlyTab
            var hourlyTab = $("<div/>", { "class": "tab-pane", id: "Hourly" });

            var hourlyOption1 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "1", name: "HourlyRadio", checked: "checked" }).appendTo(hourlyOption1);
            $(hourlyOption1).append("&nbsp;Every&nbsp;");
            $("<input/>", { id: "HoursInput", type: "text", value: "1", style: "width: 40px" }).appendTo(hourlyOption1);
            $(hourlyOption1).append("&nbsp;hour(s)");
            $(hourlyOption1).appendTo(hourlyTab);

            var hourlyOption2 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "2", name: "HourlyRadio" }).appendTo(hourlyOption2);
            $(hourlyOption2).append("&nbsp;At&nbsp;");
            $(hourlyOption2).append('<select id="AtHours" class="hours" style="width: 60px"></select>');
            $(hourlyOption2).append('<select id="AtMinutes" class="minutes" style="width: 60px"></select>');
            $(hourlyOption2).appendTo(hourlyTab);

            $(hourlyTab).appendTo(tabContent);

            //craeting the dailyTab
            var dailyTab = $("<div/>", { "class": "tab-pane", id: "Daily" });

            var dailyOption1 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "1", name: "DailyRadio", checked: "checked" }).appendTo(dailyOption1);
            $(dailyOption1).append("&nbsp;Every&nbsp;");
            $("<input/>", { id: "DaysInput", type: "text", value: "1", style: "width: 40px" }).appendTo(dailyOption1);
            $(dailyOption1).append("&nbsp;day(s)");
            $(dailyOption1).appendTo(dailyTab);

            var dailyOption2 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "2", name: "DailyRadio" }).appendTo(dailyOption2);
            $(dailyOption2).append("&nbsp;Every week day&nbsp;");
            $(dailyOption2).appendTo(dailyTab);

            $(dailyTab).append("Start time&nbsp;");
            $(dailyTab).append('<select id="DailyHours" class="hours" style="width: 60px"></select>');
            $(dailyTab).append('<select id="DailyMinutes" class="minutes" style="width: 60px"></select>');

            $(dailyTab).appendTo(tabContent);

            //craeting the weeklyTab
            var weeklyTab = $("<div/>", { "class": "tab-pane", id: "Weekly" });
            var weeklyWell = $("<div/>", { "class": "well well-small" });

            var span31 = $("<div/>", { "class": "span6 col-sm-6" });
            $("<input/>", { type: "checkbox", value: "MON" }).appendTo(span31);
            $(span31).append("&nbsp;Monday<br />");
            $("<input/>", { type: "checkbox", value: "WED" }).appendTo(span31);
            $(span31).append("&nbsp;Wednesday<br />");
            $("<input/>", { type: "checkbox", value: "FRI" }).appendTo(span31);
            $(span31).append("&nbsp;Friday<br />");
            $("<input/>", { type: "checkbox", value: "SUN" }).appendTo(span31);
            $(span31).append("&nbsp;Sunday");

            var span32 = $("<div/>", { "class": "span6 col-sm-6" });
            $("<input/>", { type: "checkbox", value: "TUE" }).appendTo(span32);
            $(span32).append("&nbsp;Tuesday<br />");
            $("<input/>", { type: "checkbox", value: "THU" }).appendTo(span32);
            $(span32).append("&nbsp;Thursday<br />");
            $("<input/>", { type: "checkbox", value: "SAT" }).appendTo(span32);
            $(span32).append("&nbsp;Saturday");

            $(span31).appendTo(weeklyWell);
            $(span32).appendTo(weeklyWell);
            //Hack to fix the well box
            $("<br /><br /><br /><br />").appendTo(weeklyWell);

            $(weeklyWell).appendTo(weeklyTab);

            $(weeklyTab).append("Start time&nbsp;");
            $(weeklyTab).append('<select id="WeeklyHours" class="hours" style="width: 60px"></select>');
            $(weeklyTab).append('<select id="WeeklyMinutes" class="minutes" style="width: 60px"></select>');

            $(weeklyTab).appendTo(tabContent);

            //craeting the monthlyTab
            var monthlyTab = $("<div/>", { "class": "tab-pane", id: "Monthly" });

            var monthlyOption1 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "1", name: "MonthlyRadio", checked: "checked" }).appendTo(monthlyOption1);
            $(monthlyOption1).append("&nbsp;Day&nbsp");
            $("<input/>", { id: "DayOfMOnthInput", type: "text", value: "1", style: "width: 40px" }).appendTo(monthlyOption1);
            $(monthlyOption1).append("&nbsp;of every&nbsp;");
            $("<input/>", { id: "MonthInput", type: "text", value: "1", style: "width: 40px" }).appendTo(monthlyOption1);
            $(monthlyOption1).append("&nbsp;month(s)");
            $(monthlyOption1).appendTo(monthlyTab);

            var monthlyOption2 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "2", name: "MonthlyRadio" }).appendTo(monthlyOption2);
            $(monthlyOption2).append("&nbsp;");
            $(monthlyOption2).append('<select id="WeekDay" class="day-order-in-month" style="width: 80px"></select>');
            $(monthlyOption2).append('<select id="DayInWeekOrder" class="week-days" style="width: 100px"></select>');
            $(monthlyOption2).append("&nbsp;of every&nbsp;");
            $("<input/>", { id: "EveryMonthInput", type: "text", value: "1", style: "width: 40px" }).appendTo(monthlyOption2);
            $(monthlyOption2).append("&nbsp;month(s)");
            $(monthlyOption2).appendTo(monthlyTab);

            $(monthlyTab).append("Start time&nbsp;");
            $(monthlyTab).append('<select id="MonthlyHours" class="hours" style="width: 60px"></select>');
            $(monthlyTab).append('<select id="MonthlyMinutes" class="minutes" style="width: 60px"></select>');

            $(monthlyTab).appendTo(tabContent);

            //craeting the yearlyTab
            var yearlyTab = $("<div/>", { "class": "tab-pane", id: "Yearly" });

            var yearlyOption1 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "1", name: "YearlyRadio", checked: "checked" }).appendTo(yearlyOption1);
            $(yearlyOption1).append("&nbsp;Every&nbsp");
            $(yearlyOption1).append('<select id="MonthsOfYear" class="months" style="width: 150px"></select>');
            $(yearlyOption1).append("&nbsp;in day&nbsp;");
            $("<input/>", { id: "YearInput", type: "text", value: "1", style: "width: 40px" }).appendTo(yearlyOption1);
            $(yearlyOption1).appendTo(yearlyTab);

            var yearlyOption2 = $("<div/>", { "class": "well well-small" });
            $("<input/>", { type: "radio", value: "2", name: "YearlyRadio" }).appendTo(yearlyOption2);
            $(yearlyOption2).append("&nbsp;The&nbsp;");
            $(yearlyOption2).append('<select id="DayOrderInYear" class="day-order-in-month" style="width: 80px"></select>');
            $(yearlyOption2).append('<select id="DayWeekForYear" class="week-days" style="width: 100px"></select>');
            $(yearlyOption2).append("&nbsp;of&nbsp;");
            $(yearlyOption2).append('<select id="MonthsOfYear2" class="months" style="width: 110px"></select>');
            $(yearlyOption2).appendTo(yearlyTab);

            $(yearlyTab).append("Start time&nbsp;");
            $(yearlyTab).append('<select id="YearlyHours" class="hours" style="width: 60px"></select>');
            $(yearlyTab).append('<select id="YearlyMinutes" class="minutes" style="width: 60px"></select>');

            $(yearlyTab).appendTo(tabContent);
            $(tabContent).appendTo(span12);

            //creating the button and results input
            resultsName = $(this).prop("id");
            $(this).prop("name", resultsName);

            $(span12).appendTo(row);
            $(row).appendTo(container);
            $(container).appendTo(mainDiv);
            $(cronContainer).append(mainDiv);

            var that = $(this);

            // Hide the original input
            that.hide();

            // Replace the input with an input group
            var $g = $("<div>").addClass("input-group");
            // Add an input
            var $i = $("<input style='display:none;'>", { type: 'text', placeholder: 'Cron trigger', readonly: 'readonly' }).addClass("form-control").val($(that).val());
            $i.appendTo($g);
            // Add the button
            var $b = $("<button class=\"btn btn-default\" style='display:none;'><i class=\"icon-edit\"></i></button>");
            // Put button inside span
            var $s = $("<span>").addClass("input-group-btn");
            $b.appendTo($s);
            $s.appendTo($g);

            $(this).before($g);

            inputElement = that;
            displayElement = $i;

            $b.popover({
                html: true,
                content: function () {
                    return $(cronContainer).html();
                },
                template: '<div class="newcronsetting"><div class="popover-content"><p></p></div></div>',
                placement: 'bottom'

            }).on('click', function (e) {
                e.preventDefault();

                fillDataOfMinutesAndHoursSelectOptions();
                fillDayWeekInMonth();
                fillInWeekDays();
                fillInMonths();
                $('#CronGenTabs a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                    //generate();
                });
                $("#CronGenMainDiv select,input").on('change',function (e) {
                  setTimeout(generate,0);
                });

                // $("#CronGenMainDiv input").on('focus',function (e) {
                //   setTimeout(generate,0);
                // });

            });
             $($b).click();
             setTimeout(function () {
               //console.log("here : "+ $("#cron").val());
               var val=$("#cron").val().split(' ');

               if(val[0]=='0' && val[1].indexOf('/')>-1) {
                 $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').addClass('active');
                 $("#MinutesInput").val(val[1][2]);

               }
               else if(val[2].indexOf('/')>-1 || (val[1]>=0 && val[2]>=0 && val[3]=='1/1')) {
                   $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
                   $('#CronGenTabs.nav-tabs li:nth-child(2), .tab-content .tab-pane:nth-child(2)').addClass('active');
                   if((val[1]>=0 || val[2]>=0) && val[2].indexOf('/')==-1) {
                     $("input:radio[name=HourlyRadio][value='2']").prop('checked','checked');
                     $("#AtHours").val((val[2].length==1?'0' : '' )+val[2]);
                     $("#AtMinutes").val((val[1].length==1?'0' : '' )+val[1]);
                   } else {
                     $("input:radio[name=HourlyRadio][value='1']").prop('checked','checked');
                     $("#HoursInput").val(val[2][2]);
                   }
               }
               else if ((val[3]!='1/1' && val[3].indexOf('/') ==1 && val[3][2]>1) || (val[5]=='MON-FRI')) {
                 $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
                 $('#CronGenTabs.nav-tabs li:nth-child(3), .tab-content .tab-pane:nth-child(3)').addClass('active');
                 if(!(val[5]=='MON-FRI')) {
                   $("#DaysInput").val(val[3][2]);
                 } else {
                   $("input:radio[name=DailyRadio][value='2']").prop('checked','checked');

                   $("#DaysInput").val('');
                 }
                 $("#DailyHours").val((val[2].length==1?'0' : '' )+val[2]);
                 $("#DailyMinutes").val((val[1].length==1?'0' : '' )+val[1]);

               }

               else if(val[5].indexOf('-')==-1 && _.intersection(val[5].split(','), ["MON","WED","FRI","SUN","TUE","THU","SAT"]).length>0) {
                 $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
                 $('#CronGenTabs.nav-tabs li:nth-child(4), .tab-content .tab-pane:nth-child(4)').addClass('active');
                 _.forEach(val[5].split(','), function (val, index) {
                   $("input:checkbox[value="+val+"]").prop('checked','checked');
                 });
                 $("#WeeklyHours").val((val[2].length==1?'0' : '' )+val[2]);
                 $("#WeeklyMinutes").val((val[1].length==1?'0' : '' )+val[1]);
               }
               else if((val[3]!='?' && val[3].indexOf('/')==-1 && val[3]>=0 && val[4].indexOf('/')==1) || (val[5].indexOf('#')>-1 && val[4].indexOf('/')==1) ) {
                 $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
                 $('#CronGenTabs.nav-tabs li:nth-child(5), .tab-content .tab-pane:nth-child(5)').addClass('active');
                 if(val[5].indexOf('#')==-1) {
                   $("#DayOfMOnthInput").val(val[3]);
                   $("#MonthInput").val(val[4][2]);

                 } else {
                   var day=val[5].split('#')[0];
                   var when=val[5].split('#')[1];
                   $("#WeekDay").val(when);
                   $("#DayInWeekOrder").val(day)
                   $("#EveryMonthInput").val(val[4][2])
                   $("input:radio[name=MonthlyRadio][value='2']").prop('checked','checked');

                 }
                 $("#MonthlyHours").val((val[2].length==1?'0' : '' )+val[2]);
                 $("#MonthlyMinutes").val((val[1].length==1?'0' : '' )+val[1]);
               }
               if(val[4]!='*' && val[4]!='?' && val[4].indexOf('/')==-1 ) {
                 $('#CronGenTabs.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
                 $('#CronGenTabs.nav-tabs li:nth-child(6), .tab-content .tab-pane:nth-child(6)').addClass('active');
                 if(val[5].indexOf('#')==-1) {
                   $("#MonthsOfYear").val(val[4]);
                   $("#YearInput").val(val[3]);
                 } else {
                   $("input:radio[name=YearlyRadio][value='2']").prop('checked','checked');
                   var day = val[5].split('#')[0];
                   var when = val[5].split('#')[1];
                   $("#DayWeekForYear").val(day);
                   $("#DayOrderInYear").val(when);
                   $("#MonthsOfYear2").val(val[4]);
                 }
                 $("#YearlyHours").val((val[2].length==1?'0' : '' )+val[2]);
                 $("#YearlyMinutes").val((val[1].length==1?'0' : '' )+val[1]);
               }
             });
            return;
        }
    });


    var fillInMonths = function () {
        var days = [
            { text: "January", val: "1" },
            { text: "February", val: "2" },
            { text: "March", val: "3" },
            { text: "April", val: "4" },
            { text: "May", val: "5" },
            { text: "June", val: "6" },
            { text: "July", val: "7" },
            { text: "August", val: "8" },
            { text: "September", val: "9" },
            { text: "October", val: "10" },
            { text: "Novermber", val: "11" },
            { text: "December", val: "12" }
        ];
        $(".months").each(function () {
            fillOptions(this, days);
        });
    };

    var fillOptions = function (elements, options) {
        for (var i = 0; i < options.length; i++)
            $(elements).append("<option value='" + options[i].val + "'>" + options[i].text + "</option>");
    };
    var fillDataOfMinutesAndHoursSelectOptions = function () {
        for (var i = 0; i < 60; i++) {
            if (i < 24) {
                $(".hours").each(function () { $(this).append(timeSelectOption(i)); });
            }
            $(".minutes").each(function () { $(this).append(timeSelectOption(i)); });
        }
    };
    var fillInWeekDays = function () {
        var days = [
            { text: "Monday", val: "MON" },
            { text: "Tuesday", val: "TUE" },
            { text: "Wednesday", val: "WED" },
            { text: "Thursday", val: "THU" },
            { text: "Friday", val: "FRI" },
            { text: "Saturday", val: "SAT" },
            { text: "Sunday", val: "SUN" }
        ];
        $(".week-days").each(function () {
            fillOptions(this, days);
        });

    };
    var fillDayWeekInMonth = function () {
        var days = [
            { text: "First", val: "1" },
            { text: "Second", val: "2" },
            { text: "Third", val: "3" },
            { text: "Fourth", val: "4" }
        ];
        $(".day-order-in-month").each(function () {
            fillOptions(this, days);
        });
    };
    var displayTimeUnit = function (unit) {
        if (unit.toString().length == 1)
            return "0" + unit;
        return unit;
    };
    var timeSelectOption = function (i) {
        return "<option id='" + i + "'>" + displayTimeUnit(i) + "</option>";
    };

    var generate = function () {
        var activeTab = $("ul#CronGenTabs li.active a").prop("id");
        var results = "";
        switch (activeTab) {
            case "MinutesTab":
                results = "0 0/" + $("#MinutesInput").val() + " * 1/1 * ? *";
                break;
            case "HourlyTab":
                switch ($("input:radio[name=HourlyRadio]:checked").val()) {
                    case "1":
                        results = "0 0 0/" + $("#HoursInput").val() + " 1/1 * ? *";
                        break;
                    case "2":
                        results = "0 " + Number($("#AtMinutes").val()) + " " + Number($("#AtHours").val()) + " 1/1 * ? *";
                        break;
                }
                break;
            case "DailyTab":
                switch ($("input:radio[name=DailyRadio]:checked").val()) {
                    case "1":
                        results = "0 " + Number($("#DailyMinutes").val()) + " " + Number($("#DailyHours").val()) + " 1/" + $("#DaysInput").val() + " * ? *";
                        break;
                    case "2":
                        results = "0 " + Number($("#DailyMinutes").val()) + " " + Number($("#DailyHours").val()) + " ? * MON-FRI *";
                        break;
                }
                break;
            case "WeeklyTab":
                var selectedDays = "";
                $("#Weekly input:checkbox:checked").each(function () { selectedDays += $(this).val() + ","; });
                if (selectedDays.length > 0)
                    selectedDays = selectedDays.substr(0, selectedDays.length - 1);
                results = "0 " + Number($("#WeeklyMinutes").val()) + " " + Number($("#WeeklyHours").val()) + " ? * " + selectedDays + " *";
                break;
            case "MonthlyTab":
                switch ($("input:radio[name=MonthlyRadio]:checked").val()) {
                    case "1":
                        results = "0 " + Number($("#MonthlyMinutes").val()) + " " + Number($("#MonthlyHours").val()) + " " + $("#DayOfMOnthInput").val() + " 1/" + $("#MonthInput").val() + " ? *";
                        break;
                    case "2":
                        results = "0 " + Number($("#MonthlyMinutes").val()) + " " + Number($("#MonthlyHours").val()) + " ? 1/" + Number($("#EveryMonthInput").val()) + " " + $("#DayInWeekOrder").val() + "#" + $("#WeekDay").val() + " *";
                        break;
                }
                break;
            case "YearlyTab":
                switch ($("input:radio[name=YearlyRadio]:checked").val()) {
                    case "1":
                        results = "0 " + Number($("#YearlyMinutes").val()) + " " + Number($("#YearlyHours").val()) + " " + $("#YearInput").val() + " " + $("#MonthsOfYear").val() + " ? *";
                        break;
                    case "2":
                        results = "0 " + Number($("#YearlyMinutes").val()) + " " + Number($("#YearlyHours").val()) + " ? " + $("#MonthsOfYear2").val() + " " + $("#DayWeekForYear").val() + "#" + $("#DayOrderInYear").val() + " *";
                        break;
                }
                break;
        }

        // Update original control
        console.log("before : "+$("#cron").val());
        inputElement.val(results);
        // Update display
        displayElement.val(results);
        console.log("after : "+$("#cron").val());

    };
    // var val = $("#cron").val();
    // var myString = val.indexOf("/")
    // var lastslash = val.lastIndexOf("/")
    // var question = val.indexOf("?")
    // var hyphen = val.indexOf("-")
    // var star = val.indexOf("*")
    // var laststar = val.lastIndexOf("*")
    // var hash = val.indexOf("#")


    // setTimeout(function(){
    //
    // if(myString==3){
    //   alert('minute');
    //   $('.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').addClass('active');
    // //  $('.nav-tabs li:nth-child(2), .tab-content .tab-pane:nth-child(2)').addClass('active');
    // }
    //
    //  else if(myString==5){
    //    alert('hourly');
    //    $('.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
    //   $('.nav-tabs li:nth-child(2), .tab-content .tab-pane:nth-child(2)').addClass('active');
    //    $('input[name=HourlyRadio][value=1]').prop('checked', true);
    //  }
    //  else if(myString>=7 && myString<=9){
    //    alert('hourly');
    //    $('.nav-tabs li:nth-child(1), .tab-content .tab-pane:nth-child(1)').removeClass('active');
    //   $('.nav-tabs li:nth-child(2), .tab-content .tab-pane:nth-child(2)').addClass('active');
    //    $('input[name=HourlyRadio][value=2]').prop('checked', true);
    //  }
    //
    // }, 0);
})(jQuery);
