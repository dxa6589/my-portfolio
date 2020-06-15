// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random quote to the page.
 */
function addRandomQuote() {
  const quotes =
      ["Hello world!", "That's rough buddy", "She didn't say you did this, she said Hugh did this", "The grass is not always greener on the other side, it's greener where you water it", "The krusty krab pizza is the pizza for you and me", "What the foop? It's time to go girl"];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}

function expand(num, section){
    var coll = document.getElementsByClassName("collapsible")[num];
    var content = document.getElementById(section);
    
    if (content.style.display === "block") {
        content.style.display = "none";
        coll.style.transform = "rotate(180deg)";
    } else {
        content.style.display = "block";
        coll.style.transform = "rotate(0deg)";
    }
}

function getComments() {
  fetch('/data').then(response => response.json()).then(list => {
      console.log("List received from server");

      const comments = document.getElementById('comments-list');
      comments.innerHTML = '';
      console.log("comments list created");

      list.forEach(item => comments.appendChild(createListElement(item)))
      console.log("comments list filled");
    });

/** Creates an <li> element containing a comment. */
function createListElement(item) {
  const liElement = document.createElement('li');
  liElement.style.padding = "10px";
  var name = item[0].replace(/</g, "&lt");
  var comment = item[1].replace(/</g, "&lt");
  liElement.innerHTML = "<strong>" + name + "</strong> " + comment + "<hr>";
  return liElement;
}

}