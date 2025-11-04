<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Goods page</title>
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
<h1>Goods page</h1>
<table style="width: 100%" border="1" id="goodstable">
    <tr id="headerrow">
        <td>Id</td>
        <td>Name</td>
        <td>Price</td>
        <td>Quantity</td>
        <td>Supplier</td>
        <td>Action</td>
    </tr>
    <tr>
        <td></td>
        <td><input type="text" name="goodsName" id="goodsName"></td>
        <td><input type="text" name="goodsPrice" id="goodsPrice"></td>
        <td><input type="number" name="goodsQuantity" id="goodsQuantity"></td>
        <td>
            <select name="supplierSelect" id="supplierSelect">
                <option value="">Loading...</option>
            </select>
        </td>
        <td><input type="button" id="addGoodsButton" value="Add Goods"></td>
    </tr>
</table>

<script
        src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous">
</script>

<script type="text/javascript">
    var allSuppliers = [];

    function loadGoods() {
        jQuery.ajax({
            url: 'rest/goods/getAllGoods',
            dataType: 'json',
            type: 'GET',
            success: function (data) {
                jQuery('.datarow').remove();
                jQuery('#headerrow').after(createDataRowsFromJson(data));
            }
        });
    }

    function loadSuppliers() {
        var $select = jQuery('#supplierSelect');
        jQuery.ajax({
            url: 'rest/supplier/getAllSuppliers',
            dataType: 'json',
            type: 'GET',
            success: function (data) {
                allSuppliers = data;
                $select.empty();
                $select.append('<option value="">-- Select supplier --</option>');
                jQuery.each(data, function (index, supplier) {
                    $select.append('<option value="' + supplier.id + '">' + supplier.name + '</option>');
                });
            },
            error: function () {
                $select.empty();
                $select.append('<option value="">Failed to load</option>')
            }
        });
    }

    function createDataRowsFromJson(data) {
        var tableContent = "";
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                var supplierId = data[key].supplier ? data[key].supplier.id : "";
                var supplierName = data[key].supplier ? data[key].supplier.name : "N/A";
                tableContent += '<tr class="datarow" data-goodsid="' + data[key].id + '" data-supplier-id="' + supplierId + '">';
                tableContent += "<td>" + data[key].id  + "</td>";
                tableContent += "<td>" + data[key].name + "</td>";
                tableContent += "<td>" + data[key].price + "</td>";
                tableContent += "<td>" + data[key].quantity + "</td>";
                tableContent += "<td>" + supplierName + "</td>";
                tableContent += "<td>";
                tableContent += "<input type='button' onclick='deleteGoods(" + data[key].id + ")' value='Delete'/>";
                tableContent += "<input type='button' onclick='editGoods(this)' value='Edit'/>";
                tableContent += "</td>";
                tableContent += "</tr>";
            }
        }
        return tableContent;
    }

    function deleteGoods(id) {
        if (!confirm("Are you sure you want to delete this goods?")) {
            return;
        }

        var url = 'rest/goods/deleteGoods/' + id;
        jQuery.ajax({
            url: url,
            type: 'DELETE',
            success: function () {
                loadGoods();
            }
        });
    }

    function editGoods(button) {
        var $row = jQuery(button).closest('tr');
        var currentSupplierId = $row.data('supplier-id');

        $row.children().each(function (index, value) {
            if(index == 1) {
                jQuery(this).html("<input type='text' id='editName' value='" + jQuery(this).text() + "' />");
            } else if (index == 2) {
                jQuery(this).html("<input type='text' id='editPrice' value='" + jQuery(this).text() + "' />");
            } else if (index == 3) {
                jQuery(this).html("<input type='number' id='editQuantity' value='" + jQuery(this).text() + "' />");
            } else if (index === 4) {
                var selectHTML = '<select id="editSupplier">';
                selectHTML += '<option value="">-- Select --</option>';
                jQuery.each(allSuppliers, function (i, supplier) {
                    var selected = (supplier.id == currentSupplierId) ? ' selected' : '';
                    selectHTML += '<option value="' + supplier.id + '"' + selected + '>' + supplier.name + '</option>';
                });
                selectHTML += '</select>';
                jQuery(this).html(selectHTML);
            } else if (index == 5) {
                var actionHtml = "<input type='button' onclick='applyEditGoods(this)' value='Update' />";
                actionHtml += "<input type='button' onclick='cancelEditGoods(this)' value='Cancel' />";
                jQuery(this).html(actionHtml);
            }
        });
    }

    function applyEditGoods(button) {
        var $row = jQuery(button).closest('tr');

        var id = $row.data('goodsid');
        var name = $row.find('#editName').val();
        var price = $row.find('#editPrice').val();
        var quantity = $row.find('#editQuantity').val();
        var supplierId = $row.find('#editSupplier').val();

        if (!name || !price || !quantity || !supplierId) {
            alert("Please, fill in all fields.");
            return;
        }

        var url = 'rest/goods/updateGoods/id/' + id +
                  '/name/' + name +
                  '/price/' + price +
                  '/quantity/' + quantity +
                  '/supplier/' + supplierId;

        jQuery.ajax({
            url: url,
            type: 'POST',
            success: function () {
                loadGoods();
            },
            error: function () {
                alert("Error while updating goods.");
                loadGoods();
            }
        });
    }

    function cancelEditGoods(button) {
        loadGoods();
    }

    jQuery(document).ready(function () {
        loadGoods();
        loadSuppliers();
        jQuery("#addGoodsButton").click(function () {
            var name = jQuery("#goodsName").val();
            var price = jQuery("#goodsPrice").val();
            var quantity = jQuery("#goodsQuantity").val();
            var supplierId = jQuery("#supplierSelect").val();

            if(!name || !price || !quantity || !supplierId) {
                alert("Please, fill in all fields.");
                return;
            }

            var url = 'rest/goods/addGoods/name/' + name +
                      '/price/' + price +
                      '/quantity/' + quantity +
                      '/supplier/' + supplierId;
            jQuery.ajax({
                url: url,
                type: 'PUT',
                success: function () {
                    jQuery("#goodsName").val('');
                    jQuery("#goodsPrice").val('');
                    jQuery("#goodsQuantity").val('');
                    jQuery("#supplierSelect").val('');
                    loadGoods();
                }
            });
        });
    });
</script>
</body>
</html>
