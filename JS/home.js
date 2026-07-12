    function goToAddCustomer() {
        window.location.href = "/customers/add";
    }

    function goToViewCustomer() {
        alert("/customers/add");
    }


async function logout()
{
    const response = await fetch ('/logout',
    {
    method: "post"
    });

if(response.ok)
{
 window.location.href="/login";
}
}