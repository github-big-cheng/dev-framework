
const req = require.context('./', false, /\.vue$/)

const requireAll = requireContext => requireContext.keys().map(requireContext);

const components = {};

requireAll(req).forEach(element => {
    components[element.default.name] = element.default
});

export default components