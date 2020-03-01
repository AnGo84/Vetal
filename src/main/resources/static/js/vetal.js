function initDatePicker(datePicker) {
    //from
    //https://uxsolutions.github.io/bootstrap-datepicker/?markup=input&format=dd-mm-yyyy&weekStart=&startDate=&endDate=&startView=0&minViewMode=0&maxViewMode=4&todayBtn=false&clearBtn=false&language=ru&orientation=auto&multidate=&multidateSeparator=&autoclose=on&todayHighlight=on&keyboardNavigation=on&forceParse=on#sandbox
    $(datePicker).datepicker({
        format: "dd.mm.yyyy",
        autoclose: true,
        todayHighlight: true,
        language: "ru",
        weekStart: 1
    });
};

function initTable(tableName, ordering, sortAsData) {
    if (sortAsData !== undefined && sortAsData !== null) {
        //https://datatables.net/blog/2014-12-18
        //https://datatables.net/plug-ins/sorting/
        $.fn.dataTable.moment('DD.MM.YYYY');
    }

    if (ordering === undefined || ordering === null) {
        ordering = [0, "asc"];
    }
    //from
    //https://datatables.net/examples/basic_init/table_sorting.html
    $(tableName).DataTable({
        "language": {
            "lengthMenu": "Показать _MENU_ записей на странице",
            "zeroRecords": "Ничего не найдено",
            "info": "Показана страница _PAGE_ из _PAGES_",
            "infoEmpty": "Нет доступных записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "search": "Быстрый поиск",
            "paginate": {
                "first": "Первая",
                "last": "Последняя",
                "next": "Следующая",
                "previous": "Предыдущая"
            }
        },
        "order": [ordering],
        "pagingType": "full_numbers",
        "lengthMenu": [[10, 15, 20, 25, 50, -1], [10, 15, 20, 25, 50, "Все"]],
        "iDisplayLength": 15,
        "aoColumnDefs": [
            {'bSortable': false, 'aTargets': ['no-sort']}
        ]
        /*,
        responsive: true*/
    });
};

/*function setTableOrdering(tableName, ordering) {
    $(tableName).DataTable( {

    });

} ;*/
