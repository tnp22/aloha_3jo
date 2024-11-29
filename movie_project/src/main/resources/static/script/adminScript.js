$(function(){

    $(".mainLi").on("mouseover",function(){
        $(".subLi").stop().slideDown()
    })
    $(".mainLi").on("mouseout",function(){
        $(".subLi").stop().slideUp()
    })

})