document.addEventListener('DOMContentLoaded', ()=>{
    document.getElementById('btnRadio1').addEventListener('change', eventNotEvent);
    document.getElementById('btnRadio2').addEventListener('change', eventNotEvent);

    eventNotEvent();
    });

function eventNotEvent(){
    var isDisable = document.getElementById('btnRadio2').checked;
    document.getElementById('InputEventPlace').disabled = isDisable;
    document.getElementById('InputEventDescription').disabled = isDisable;

}