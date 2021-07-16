var container = document.getElementById("mynetwork1");
var networkNodes = [];
var networkEdges = [];
var data = {
  nodes: networkNodes,
  edges: networkEdges,
};
var options = {layout: {hierarchical:{direction: 'UD',sortMethod: "directed"}},interaction:{multiselect: true},edges:{ arrows:{to: {enabled: true}},}};
var network = new vis.Network(container, data, options);

function destroy() {
  if (network !== null) {
    network.destroy();
    network = null;
  }
}

function createNetwork(edges,attr) {
    // create an array with nodes
//    var nodes = new vis.DataSet([
//      { id: 1, label: "Node 1" },
//      { id: 2, label: "Node 2" },
//      { id: 3, label: "Node 3" },
//      { id: 4, label: "Node 4" },
//      { id: 5, label: "Node 5" },
//    ]);
//
//    // create an array with edges
//    var edges = new vis.DataSet([
//      { from: 1, to: 1 },
//      { from: 1, to: 2 },
//      { from: 3, to: 4 },
//      { from: 4, to: 5 },
//      { from: 3, to: 3 },
//    ]);
    console.log(edges);
    console.log(attr);
    networkNodes = attr;

    // create an array with edges
    networkEdges = edges;

    // create a network
//    var container = document.getElementById("mynetwork1");
    var data = {
      nodes: new vis.DataSet(attr),
      edges: new vis.DataSet(edges),
    };
//    var options = {interaction:{multiselect: true}};
    network = new vis.Network(container, data, options);
}

function focusNetwork(){
    nodeLabel = document.getElementById('message').value;
//    console.log(nodeLabel);
//    console.log(networkEdges);
//    console.log(networkNodes.find(o => o.label === nodeLabel)['id']);
    network.focus(networkNodes.find(o => o.label === nodeLabel)['id']);
}