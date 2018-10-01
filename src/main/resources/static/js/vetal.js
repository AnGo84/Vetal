function initDatePicker(datePicker) {
    //from
    //https://uxsolutions.github.io/bootstrap-datepicker/?markup=input&format=dd-mm-yyyy&weekStart=&startDate=&endDate=&startView=0&minViewMode=0&maxViewMode=4&todayBtn=false&clearBtn=false&language=ru&orientation=auto&multidate=&multidateSeparator=&autoclose=on&todayHighlight=on&keyboardNavigation=on&forceParse=on#sandbox
    $(datePicker).datepicker({
        format: "dd.mm.yyyy",
        autoclose: true,
        todayHighlight: true,
        language: "ru"
    });
};