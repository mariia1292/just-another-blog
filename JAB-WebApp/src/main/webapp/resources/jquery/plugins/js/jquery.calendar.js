/* *
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


(function($){

  $.fn.extend({
    calendar: function( url, options ){
      return this.each( function(){
        new $.Calendar(this, url, options);
      });
    }

  });

  $.Calendar = function(field, url, options){

    var defaults = {
      dayUrlPattern : "?year={0}&month={1}&day={2}",
      monthUrlPattern : "?year={0}&month={1}",
      yearUrlPattern : "?year={0}",
      dayLabels : ["Mo","Tu","We","Th","Fr","Sa","Su"],
      loadingImage : null,
      cache: true
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var cache = null;
    if ( options.cache ){
      cache = [];
    }

    monthView(null, null);

    function monthView(year, month){
      loadingView();
      var postUrl = url;
      if ( year == null )
      {
        year = new Date().getFullYear();
      }
      if ( month == null )
      {
        month = new Date().getMonth() + 1;
      }
      postUrl += "?year=" + year + "&month=" + month;

      if ( cache != null ){
        var result = cache[postUrl];
        if ( result != null ){
          clearLoadingView();
          createCalendar(result);
        } else {
          request( postUrl );
        }
      } else {
        request( postUrl );
      }
    }

    function request(postUrl){
      $.getJSON(postUrl, function(result){
        if ( cache != null ){
          cache[postUrl] = result;
        }
        clearLoadingView();
        createCalendar(result);
      });
    }

    function createCalendar(calendar){
      var today = calendar.today;
      var todayIsSet = false;
      var events = calendar.events.split(",");
      var counter = 0;
      var $table = $("<table />");
      appendTableHeader(calendar, $table);
      appendDayHeadings($table);
      var weeks = parseInt(calendar.weeks);
      var firstDay = calendar.firstDay - 2;
      if ( firstDay < 0 ){
        firstDay = 6;
        weeks++;
      }

      for ( var i=0; i<weeks; i++ ){
        
        var $tr = $("<tr />");
        for (var j=0; j<7; j++){
          
          var $td = $("<td />");

          if (( i == 0 && j >= firstDay) ||
             ( counter > 0 && counter < calendar.daysOfMonth )){
             counter++;
            if ( hasEvent(events, counter)){
              $td.append(
                $("<a />").attr("href", createDayLink(calendar, counter)).text(counter)
              )
            } else {
              $td.text(counter);
            }
          }

          if ( today == counter && ! todayIsSet ){
            $td.addClass("today");
            todayIsSet = true;
          }

          $tr.append($td);
        }
        $table.append($tr);
      }
      $field.append(
        $table
      );
    }

    function appendDayHeadings($tbody){
      var $tr = $("<tr />")
      for (var i=0;i<options.dayLabels.length; i++){
        $tr.append(
          $("<th />").text( options.dayLabels[i] )
        );
      }
      $tbody.append($tr);
    }

    function appendTableHeader(calendar, $table){
      $table.append(
        $("<tr />").append(
          $("<th />").addClass("header").attr("colSpan", "7").append(
            $("<a />").text("<<").click(function(){
              previousYear(calendar);
            })
          ).append(
            " "
          ).append(
            $("<a />").text("<").click(function(){
              previousMonth(calendar);
            })
          ).append(
            " "
          ).append(
            $("<a />").attr("href", createYearLink(calendar)).text(calendar.year)
          ).append(
            " "
          ).append(
            $("<a />").attr("href", createMonthLink(calendar)).text(calendar.month)
          ).append(
            " "
          ).append(
            $("<a />").text(">").click(function(){
              nextMonth(calendar);
            })
          ).append(
            " "
          ).append(
            $("<a />").text(">>").click(function(){
              nextYear(calendar);
            })
          )
        )
      );
    }

    function nextYear(calendar){
      var year = parseInt(calendar.year) + 1;
      monthView(year ,calendar.month);
    }

    function previousYear(calendar){
      var year = parseInt(calendar.year) - 1;
      monthView(year ,calendar.month);
    }

    function nextMonth(calendar){
      var year = parseInt(calendar.year);
      var month = parseInt(calendar.month) + 1;
      if (month > 12){
        year++;
        month = 1;
      }
      monthView(year, month);
    }

    function previousMonth(calendar){
      var year = parseInt(calendar.year);
      var month = parseInt(calendar.month) - 1;
      if (month <= 0){
        year--;
        month = 12;
      }
      monthView(year, month);
    }

    function createYearLink(calendar){
      var eventUrl = options.yearUrlPattern;
      eventUrl = eventUrl.replace("{0}", calendar.year);
      return eventUrl;
    }

    function createMonthLink(calendar){
      var eventUrl = options.monthUrlPattern;
      eventUrl = eventUrl.replace("{0}", calendar.year);
      eventUrl = eventUrl.replace("{1}", calendar.month);
      return eventUrl;
    }

    function createDayLink(calendar, day){
      var eventUrl = options.dayUrlPattern;
      eventUrl = eventUrl.replace("{0}", calendar.year);
      eventUrl = eventUrl.replace("{1}", calendar.month);
      eventUrl = eventUrl.replace("{2}", day);
      return eventUrl;
    }

    function hasEvent(events, day){
      for (var i=0;i<events.length; i++){
        if ( day == events[i] ){
          return true;
        }
      }
      return false;
    }

    function loadingView(){
      $field.empty();
      if ( options.loadingImage != null ){
        $field.append(
          $("<div />").addClass("load").append(
            $("<img />").attr("src", options.loadingImage)
          )
        );
      }
    }

    function clearLoadingView(){
      $("div.load").remove();
    }

  }


})(jQuery);