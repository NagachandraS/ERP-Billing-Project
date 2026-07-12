async function deleteProduct(id)
{
    if(!confirm ("Are you sure you want to delete this Product?"))
        return;
    try
    {
        const response = await fetch (`/api/products/${id}`,{method: "DELETE"} )
        if(response.ok)
        {
            window.location.reload();
            alert("products delete successfully");
        }

        else{
            alert("products failed to delete");
        }
    }
    catch (error)
    {
        console.log(error);
        console.log("Something went wrong");
    }
}


function setupFieldValidation(inputId, url, validationMessageId)
{
    document.getElementById(inputId).addEventListener("blur", function()
    {
        const typedData = this.value;

        if(!typedData)
        {
            return;
        }

        apiCall(
            url + typedData,
            "GET",
            null,
            function(result)
            {
                const validationMessage = document.getElementById(validationMessageId);
                validationMessage.innerText = result.message;

                if(result.data)
                {
                    validationMessage.style.color = "red";
                }
                else
                {
                    validationMessage.style.color = "green";
                }
            },
            false
        );
    });
}

setupFieldValidation(
    "barCode",
    "/check/barcode/",
    "barcodeValidationMessage"
);
setupFieldValidation(
    "productCode",
    "/check/productCode/",
    "productCodeValidationMessage"
);

