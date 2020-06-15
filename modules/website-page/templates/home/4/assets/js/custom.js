/*==========

Template Name: FoodeCart - Restaurants & Food Template

==========*/

/*==========
----- JS INDEX -----
1.Whole Script Strict Mode Syntax
2.Loader JS
3.Wow Animation JS
4.Banner Slider JS
5.Gallery Slider JS
6.Team Slider JS
7.Testimonial Slider JS
8.Smooth Scrolling JS
9.Scroll To Top JS
10.Sticky Header JS
11.Toogle Menu Mobile JS
12.Blog Slider For Mobile JS
13.Date Picker JS
14.Time Picker JS
==========*/



$(document).ready(function($) {

	// Whole Script Strict Mode Syntax
	"use strict";

	// Loader JS Start
	$(window).ready(function(){
	     $('.loader-box').fadeOut();
	});
	// Loader JS End

	// Wow Animation JS Start
	new WOW().init(); 
	// Wow Animation JS Start

	// Banner Slider JS Start
	$('.banner-slider').slick({
	  infinite: true,
	  slidesToShow: 1,
	  slidesToScroll: 1,
	  prevArrow: false,
	  nextArrow: false,
	  dots: false,
	  autoplay: true,
  	  autoplaySpeed: 5000,	
	  speed: 1000,
	  fade: true,
	  cssEase: 'linear'
	});
	// Banner Slider JS End

	// Gallery Slider JS Start
	$('.instagram-slider').slick({
	  infinite: true,
	  slidesToShow: 6,
	  slidesToScroll: 1,
	  prevArrow: false,
	  nextArrow: false,
	  autoplay: true,
  	  autoplaySpeed: 2000,
  	  responsive: [
  	  {
  	  	breakpoint: 1500,
  	  	settings: {
  	  		slidesToShow: 5
  	  	}
  	  },
  	  {
  	  	breakpoint: 1400,
  	  	settings: {
  	  		slidesToShow: 4
  	  	}
  	  },
  	  {
  	  	breakpoint: 992,
  	  	settings: {
  	  		slidesToShow: 3
  	  	}
  	  },
  	  {
  	  	breakpoint: 768,
  	  	settings: {
  	  		slidesToShow: 2
  	  	}
  	  },
  	  {
  	  	breakpoint: 576,
  	  	settings: {
  	  		slidesToShow: 1
  	  	}
  	  }
    ]	
	});
	// Gallery Slider JS Start

	// Team Slider JS Start
	$('.chefs-slider').slick({
	  infinite: true,
	  slidesToShow: 3,
	  slidesToScroll: 1,
	  prevArrow: false,
	  nextArrow: false,
	  dots: true,
	  autoplay: false,
  	  autoplaySpeed: 2000,	
  	  responsive: [
  	  {
  	  	breakpoint: 992,
  	  	settings: {
  	  		slidesToShow: 2
  	  	}
  	  },
  	  {
  	  	breakpoint: 768,
  	  	settings: {
  	  		slidesToShow: 1
  	  	}
  	  },
  	  {
  	  	breakpoint: 576,
  	  	settings: {
  	  		slidesToShow: 1
  	  	}
  	  }
    ]
	});
	// Team Slider JS End

	// Testimonial Slider JS Start
	$('.testimonial-slider').slick({
	  centerMode: true,
	  infinite: true,
	  slidesToShow: 3,
	  slidesToScroll: 1,
	  prevArrow: false,
	  nextArrow: false,
	  dots: true,
	  autoplay: true,
  	  autoplaySpeed: 2000,
  	  responsive: [
  	  {
  	  	breakpoint: 992,
  	  	settings: {
  	  		slidesToShow: 1
  	  	}
  	  }
    ]	
	});
	// Testimonial Slider JS End

	// Smooth Scrolling JS Start
	$( '.menu-item a, .scroll-down a, #scroll-top, .footer-menu ul li a' ).on( 'click', function(e){		
	  var href = $(this).attr( 'href' );	  
	  $( 'html, body' ).animate({
			scrollTop: $( href ).offset().top
	  }, '800' );
	  e.preventDefault();

	});
	// Smooth Scrolling JS End

	// Scroll To Top JS Start
	$(window).on( 'scroll', function() {
	  if ($(window).scrollTop() > 300) {
	    $("#scroll-top").fadeIn('500');
	  } else {
	    $("#scroll-top").fadeOut('500');
	  }
	});
	// Scroll To Top JS End

	// Sticky Header JS Start
	$(window).on( 'scroll', function(){
	  if ($(window).scrollTop() >= 200) {
	    $('.site-header').addClass('sticky-header');
	   }
	   else {
	    $('.site-header').removeClass('sticky-header');
	   }
	});
	// Sticky Header JS End

	// Toogle Menu Mobile JS Start
	$(".toggle-button").on( 'click', function(){
		$(".main-navigation").toggleClass('toggle-menu');
		$(".black-shadow").fadeToggle();
	});
	$(".menu-item a").on( 'click', function(){
		$(".main-navigation").removeClass('toggle-menu');
		$(".black-shadow").fadeOut();
	});
	$(".black-shadow").on( 'click', function(){
		$(this).fadeOut();
		$(".main-navigation").removeClass('toggle-menu');
	});
	// Toogle Menu Mobile JS End

	// Blog Slider For Mobile JS Start
	if ($(window).width() < 992) {
	   $('.blog-slider').slick({
		  infinite: true,
		  slidesToShow: 2,
		  slidesToScroll: 1,
		  prevArrow: false,
		  nextArrow: false,
		  dots: true,
		  autoplay: false,
	  	  autoplaySpeed: 2000,	
	  	  responsive: [
	  	  {
	  	  	breakpoint: 768,
	  	  	settings: {
	  	  		slidesToShow: 1
	  	  	}
	  	  }
	    ]
		});
	}
	// Blog Slider For Mobile JS End

	// Date Picker JS Start
    $('#datepicker').datetimepicker({
		format: 'DD/MM/YYYY',
		minDate: new Date(),
	});
	// Date Picker JS End

    // Time Picker JS Start
	$('#timepicker').datetimepicker({
	 	format: 'hh:mm A',
	});
    // Time Picker JS End

});