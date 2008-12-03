package holmes.dao {

	public interface IDao {

		/**
		 * Updates Model.instance.catalogs and Model.instance.plans
		 */
		function fetchData(): void;

	}
}