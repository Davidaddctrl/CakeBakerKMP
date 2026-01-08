const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const version = Number.parseInt(urlParams.get('version'));

window.onload = () => {
    fetch("./builds.json").then((value) => {
            value.text().then((text) => {
                    let builds = JSON.parse(text);
                    console.log(builds)
                    console.log(version)
                    loadBuildsAndVersion(builds, version);
                }
            )
        }
    )
}

function loadBuildsAndVersion(builds, version) {
    let index = 0
    builds.forEach((build) => {
        let thisVersion = index++
        let button = $("<button>")
            .text(build.version).click(() => {
                window.location.href = "?version=" + thisVersion
            }).addClass("version-item")
        $('#version-select').append(button)
        if (thisVersion == version)
            build.platforms.forEach((platform) => {
                const downloadLink = platform.downloadLink
                const playLink = platform.playLink
                const platformTitle = $("<div>").text(platform.name).addClass("build-item-title")
                const downloadButton = $("<button>")
                    .text("Download")
                    .addClass("build-item-bottom")
                    .addClass("secondary-button")
                    .click(() => {
                        const a = document.createElement("a");
                        a.href = downloadLink
                        document.body.appendChild(a);
                        a.click();
                        document.body.removeChild(a);
                    })
                let runButton;
                if (playLink) {
                    runButton = $("<button>")
                        .text("Play in Browser")
                        .addClass("build-item-bottom")
                        .addClass("secondary-button")
                        .click(() => {
                            const a = document.createElement("a");
                            a.href = playLink
                            document.body.appendChild(a);
                            a.click();
                            document.body.removeChild(a);
                        })
                } else {
                    runButton = null
                }
                const bottomRow = $("<div>").addClass("build-item-bottom")
                if (runButton) bottomRow.append(runButton)
                bottomRow.append(downloadButton)

                const buildItem = $("<div>")
                    .addClass("build-item").addClass("secondary-container")
                    .append(platformTitle)
                    .append(bottomRow)
                $("#builds-list").append(buildItem)
            })
    })
    index = 0
    $('#version-select .version-item').each(function () {
        if (index++ == version) {
            $(this).css("font-weight", "bolder");
            $(this).css("color", "white");
        }
    });
}