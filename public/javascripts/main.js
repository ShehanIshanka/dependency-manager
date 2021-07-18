var container = document.getElementById("dependencyGraph");
var networkNodes = [];
var networkEdges = [];
var data = {
  nodes: networkNodes,
  edges: networkEdges,
};
var options = {layout: {hierarchical:{direction: 'UD',sortMethod: "directed"}},interaction:{multiselect: true},edges:{ arrows:{to: {enabled: true}},}};
var network = new vis.Network(container, data, options);

function setNetworkDirection(direction) {
  options.layout.hierarchical.direction = direction;
  createNetwork(networkEdges,networkNodes);
}

function destroy() {
  if (network !== null) {
    network.destroy();
    network = null;
  }
}

function createNetwork(edges,attr) {
    destroy();

    networkNodes = attr;
    networkEdges = edges;

    var data = {
      nodes: new vis.DataSet(attr),
      edges: new vis.DataSet(edges),
    };
    network = new vis.Network(container, data, options);
}

function focusNetwork(){
    nodeLabel = document.getElementById('message').value;
    network.focus(networkNodes.find(o => o.label === nodeLabel)['id']});
}