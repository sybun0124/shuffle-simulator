$(function (){
  $(document).ready(function(){
    changeDisplayNumber();
  });
  $("#shuffleType").change(function() {
    changeDisplayNumber();
  });

  function changeDisplayNumber(){
    // value値を取得
    const value = $("#shuffleType").val();
    if(value == 'DEAL'){
      $("#numberField").removeClass("d-none");
    }else{
      $("#numberField").addClass("d-none");
    }
  }
});