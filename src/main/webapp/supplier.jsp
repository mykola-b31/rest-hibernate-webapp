<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Supplier page</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        h1 { color: #333; }
        nav { margin-bottom: 20px; background-color: #eee; padding: 10px; border-radius: 5px; }
        nav a { text-decoration: none; color: #007BFF; padding: 10px; font-weight: bold; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 3px rgba(0,0,0,0.1);
        }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #007BFF; color: white; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        table tr:last-child { background-color: #fff; }
        input[type=text], input[type=number], select {
            width: 95%; padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        input[type=button] {
            background-color: #007BFF;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin: 2px;
        }
        input[value=Delete], input[value=Cancel] {
            background-color: #6c757d;
        }
    </style>
</head>
<body>
<nav>
    <a href="goods.jsp">Goods</a>
    <a href="supplier.jsp">Suppliers</a>
</nav>
<h1>Supplier Page</h1>
<table style="width: 100%" border="1" id="supplierTable">
    <tr id="headerrow">
        <td>Id</td>
        <td>Name</td>
        <td>Contact</td>
        <td>Address</td>
        <td>Specialization</td>
        <td>Action</td>
    </tr>
    <tr>
        <td></td>
        <td><input type="text" name="supplierName" id="supplierName"></td>
        <td><input type="text" name="supplierContact" id="supplierContact"></td>
        <td><input type="text" name="supplierAddress" id="supplierAddress"></td>
        <td><input type="text" name="supplierSpecialization" id="supplierSpecialization"></td>
        <td><input type="button" name="addSupplier" id="addSupplier" value="Add Supplier"></td>
    </tr>
</table>

<script
        src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous">
</script>

<script type="text/javascript">

    function loadSuppliers() {
        jQuery.ajax({
            url: 'rest/supplier/getAllSuppliers',
            dataType: 'json',
            type: 'GET',
            success: function (data){
                jQuery('.datarow').remove();
                jQuery('#headerrow').after(createDataRowsFromJson(data));
            }
        });
    }

    function createDataRowsFromJson(data) {
        var tableContent = "";
        for (var key in data) {
            if(data.hasOwnProperty(key)) {
                tableContent += "<tr class='datarow' data-supplierid='" + data[key].id + "' >";
                tableContent += "<td>" + data[key].id + "</td>";
                tableContent += "<td>" + data[key].name + "</td>";
                tableContent += "<td>" + data[key].contact + "</td>";
                tableContent += "<td>" + data[key].address + "</td>";
                tableContent += "<td>" + data[key].specialization + "</td>";
                tableContent += "<td>";
                tableContent += "<input type='button' onclick='deleteSupplier(" + data[key].id + ")' value='Delete' />"
                tableContent += "<input type='button' onclick='editSupplier(this)' value='Edit' />"
                tableContent += "</td>";
                tableContent += "</tr>";
            }
        }
        return tableContent;
    }

    function deleteSupplier(id) {
        if (!confirm("Are you sure you want to delete this supplier?")) {
            return;
        }

        var url = 'rest/supplier/deleteSupplier/' + id;
        jQuery.ajax({
            url: url,
            type: 'DELETE',
            success: function (){
                loadSuppliers();
            }
        });
    }

    function editSupplier(button) {
        jQuery(button).closest('tr').children().each(function (index, value) {
            if (index == 1) {
                jQuery(this).html("<input type='text' id='editName' value='" + jQuery(this).text() + "' />");
            } else if (index == 2) {
                jQuery(this).html("<input type='text' id='editContact' value='" + jQuery(this).text() + "' />");
            } else if (index == 3) {
                jQuery(this).html("<input type='text' id='editAddress' value='" + jQuery(this).text() + "' />");
            } else if (index == 4) {
                jQuery(this).html("<input type='text' id='editSpecialization' value='" + jQuery(this).text() + "' />");
            } else if (index == 5) {
                var actionHtml = "<input type='button' onclick='applyEditSupplier(this)' value='Update' />";
                actionHtml += "<input type='button' onclick='cancelEditSupplier(this)' value='Cancel' />"
                jQuery(this).html(actionHtml);
            }
        });
    }

    function applyEditSupplier(button) {
        var $row = jQuery(button).closest('tr');

        var id = $row.data('supplierid');
        var name = $row.find('#editName').val();
        var contact = $row.find('#editContact').val();
        var address = $row.find('#editAddress').val();
        var specialization = $row.find('#editSpecialization').val();

        if(!name || !contact || !address || !specialization) {
            alert("Please, fill in all fields.");
            return;
        }

        var url = 'rest/supplier/updateSupplier/id/' + id +
                  '/name/' + name +
                  '/contact/' + contact +
                  '/address/' + address +
                  '/specialization/' + specialization;

        jQuery.ajax({
            url: url,
            type: 'POST',
            success: function (){
                loadSuppliers();
            },
            error: function (){
                alert("Error while updating supplier.");
                loadSuppliers();
            }
        });
    }

    function cancelEditSupplier(button) {
        loadSuppliers();
    }

    jQuery(document).ready(function (){
        loadSuppliers();
        jQuery('#addSupplier').click(function(){
            var name = jQuery('#supplierName').val();
            var contact = jQuery('#supplierContact').val();
            var address = jQuery('#supplierAddress').val();
            var specialization = jQuery('#supplierSpecialization').val();
            if(!name || !contact || !address || !specialization) {
                alert("Please, fill in all fields.");
                return;
            }
            var url = 'rest/supplier/addSupplier/name/' + name +
                      '/contact/' + contact +
                      '/address/' + address +
                      '/specialization/' + specialization;
            jQuery.ajax({
                url: url,
                type: 'PUT',
                success: function (){
                    jQuery('#supplierName').val('');
                    jQuery('#supplierContact').val('');
                    jQuery('#supplierAddress').val('');
                    jQuery('#supplierSpecialization').val('');
                    loadSuppliers();
                }
            });
        });
    });
</script>

</body>
</html>
