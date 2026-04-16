using BlazorApp1.Components;
using Microsoft.AspNetCore.Authentication.Cookies;
using MongoDB.Driver;
using web.PixelTalk.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/auth";
        options.Cookie.Name = "PixelTalkAuth";
    });
builder.Services.AddAuthorization();
builder.Services.AddCascadingAuthenticationState();
builder.Services.AddHttpContextAccessor();

builder.Services.AddSingleton<IMongoClient>(sp =>
{
    var connectionString = builder.Configuration.GetConnectionString("MongoDb");
    return new MongoClient(connectionString ?? "mongodb://witteringgray:27017");
});
builder.Services.AddSingleton<MongoService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}

app.UseStatusCodePagesWithReExecute("/not-found");
app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();
app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.MapGet("/login_callback", async (string code, web.PixelTalk.Services.MongoService mongoService, HttpContext context) =>
{
    var req = await mongoService.AuthRequests.Find(r => r.Code == code && r.Resolved == true).FirstOrDefaultAsync();
    if (req != null && !string.IsNullOrEmpty(req.Uuid))
    {
        Console.WriteLine(req.Uuid);
        var player = await mongoService.Players.Find(p => p.Uuid == req.Uuid).FirstOrDefaultAsync();
        var claims = new List<System.Security.Claims.Claim>
        {
            new(System.Security.Claims.ClaimTypes.NameIdentifier, req.Uuid),
            new(System.Security.Claims.ClaimTypes.Name, player?.Nickname ?? req.Uuid)
        };
        if (player?.Role == "op")
        {
            claims.Add(new System.Security.Claims.Claim(System.Security.Claims.ClaimTypes.Role, "op"));
        }
        var identity = new System.Security.Claims.ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
        var principal = new System.Security.Claims.ClaimsPrincipal(identity);
        
        await Microsoft.AspNetCore.Authentication.AuthenticationHttpContextExtensions.SignInAsync(context, CookieAuthenticationDefaults.AuthenticationScheme, principal);
        
        return Results.Redirect("/");
    }
    return Results.Redirect("/auth");
});

app.Run();