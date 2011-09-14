package org.talend.jpalo.talendHelpers;

import java.util.*;

import org.talend.jpalo.*;

public class tPaloDimensions {

	// private Set<tPaloDimensionElements> lstPaloDimensionElements = new
	// HashSet<tPaloDimensionElements>();
	private Set<String[]> lstTalendMainIn = new HashSet<String[]>();
	private ArrayList<tPaloDimensionElements> lstPaloDimensionElements = new ArrayList<tPaloDimensionElements>();
	// private List<String[]> lstTalendMainIn = new ArrayList<String[]>();

	private int iNbOfCols = 0;

	public tPaloDimensions() {
		super();
	}

	public tPaloDimensions(int iNbOfCols) {
		super();
		this.iNbOfCols = iNbOfCols;
	}

	// Takes the Talend Main in and stores it for further usage
	public void addMainInToTransformList(String[] strMainIn) {
		lstTalendMainIn.add(strMainIn);
	}

	public void buildParentChildRelationShipReferenced() {
		int iPosition = 0;
		for (String[] strMainIn : lstTalendMainIn) {
			for (int i = 0; i < 2; i++) {
				boolean bFound = false;
				for (tPaloDimensionElements tParentDimensionElement : lstPaloDimensionElements) {
					if (tParentDimensionElement.getElementName().equals(
							strMainIn[i])) {
						bFound = true;
						break;
					}
				}
				if (!bFound) {
					int iLevel = getElementLevel(strMainIn[0], 0);

					double dFactor = 1;
					// Check for factor
					if (strMainIn.length == 3 && i == 1) {
						try {
							dFactor = Double.parseDouble(strMainIn[2]);
						} catch (Exception e) {
						}
					}
					tPaloDimensionElements tDimensionElement = new tPaloDimensionElements(
							iPosition, strMainIn[i], -1, iLevel, dFactor);
					lstPaloDimensionElements.add(tDimensionElement);
					iPosition++;
				}
			}
		}
		for (String[] strMainIn : lstTalendMainIn) {
			for (tPaloDimensionElements tParentDimensionElement : lstPaloDimensionElements) {
				if (tParentDimensionElement.getElementName().equals(
						strMainIn[0])) {
					tPaloDimensionElements actElem = getDimenionElement(strMainIn[1]);
					if (null != actElem) {
						actElem.setLevel(tParentDimensionElement.getLevel() + 1);
						actElem.setParentPosition(tParentDimensionElement
								.getPosition());
					}
					break;
				}
			}
		}
	}

	private tPaloDimensionElements getDimenionElement(String strName) {
		tPaloDimensionElements rctPaloDimensionElements = null;
		for (tPaloDimensionElements tParentDimensionElement : lstPaloDimensionElements) {
			if (tParentDimensionElement.getElementName().equals(strName)) {
				rctPaloDimensionElements = tParentDimensionElement;
			}
		}
		return rctPaloDimensionElements;
	}

	private int getElementLevel(String strParentName, int iPos) {
		boolean bFound = false;
		String strX = "";
		for (String[] strMainIn : lstTalendMainIn) {
			if (strMainIn[1].equals(strParentName)
					&& !strMainIn[0].equals(strParentName)) {
				// SubLevel
				bFound = true;
				strX = strMainIn[0];
				iPos++;
				break;
			}
		}
		if (bFound) {
			return getElementLevel(strX, iPos);
		}
		return iPos;
	}

	public void buildParentChildRelationShipNormal() {

		int iPosition = 0;
		for (String[] strMainIn : lstTalendMainIn) {
			for (int i = 0; i < strMainIn.length; i++) {
				boolean bElemenExists = false;
				// Check if Dimension Element allready Exists
				for (tPaloDimensionElements tDimensionElement : lstPaloDimensionElements) {
					if (tDimensionElement.getElementName().equals(strMainIn[i])) {
						bElemenExists = true;
						break;
					}
				}
				// if(!bElemenExists){
				int iParentPosition = -1;
				// Get ParentPosision
				if (i > 0) {
					for (tPaloDimensionElements tParentDimensionElement : lstPaloDimensionElements) {
						if (tParentDimensionElement.getElementName().equals(
								strMainIn[i - 1])) {
							iParentPosition = tParentDimensionElement
									.getPosition();
							break;
						}
					}
				}
				tPaloDimensionElements tDimensionElement = new tPaloDimensionElements(
						iPosition, strMainIn[i], iParentPosition, i);
				lstPaloDimensionElements.add(tDimensionElement);
				iPosition++;
				// }
			}
		}
	}

	public void buildConsolidationNormal(paloelements paloElements,
			String strElementName, int iParentPosition) throws paloexception {

		boolean bConsolidationElementsFound = false;
		paloconsolidations paloCons = new paloconsolidations();

		for (tPaloDimensionElements tConsElement : lstPaloDimensionElements) {
			if (tConsElement.getParentPosition() == iParentPosition
					&& !tConsElement.getElementName().equals(strElementName)) {
				paloCons.addElementToConsolidation(
						paloElements.getElement(tConsElement.getElementName()),
						tConsElement.getFactor());
				bConsolidationElementsFound = true;
			}
		}
		if (bConsolidationElementsFound) {
			paloElements.getElement(strElementName).updateElement(paloCons,
					true);
		}
	}

	public List<tPaloDimensionElements> getParentChildList() {
		return lstPaloDimensionElements;
	}

	public List<tPaloDimensionElements> getParentChildListSorted() {

		elementComparator elementsComparator = new elementComparator();
		Collections.sort(lstPaloDimensionElements, elementsComparator);

		return lstPaloDimensionElements;
	}

	private class elementComparator implements
			Comparator<tPaloDimensionElements> {
		public int compare(tPaloDimensionElements o1, tPaloDimensionElements o2) {
			if (o1.getLevel() > o2.getLevel())
				return 1;
			else
				return -1;
		}
	}

	public void createBulk(paloelements paloElements) throws paloexception {
		try {

			for (int x = iNbOfCols; x > 1; x--) {

				HashMap<String, HashSet<String>> al = new HashMap<String, HashSet<String>>();
				for (String[] strArr : lstTalendMainIn) {
					String strParent = strArr[x - 2];
					String strChild = strArr[x - 1];
					if (al.containsKey(strParent) == false) {
						al.put(strParent, new HashSet<String>());
					}
					al.get(strParent).add(strChild);
				}

				// System.out.println(al==null);
				Set<?> set = al.entrySet();
				Iterator<?> i = set.iterator();
				while (i.hasNext()) {
					Map.Entry me = (Map.Entry) i.next();

					paloelement plElm = paloElements.getElement((String) me
							.getKey());
					// System.out.println(null==plElm);
					if (null != plElm) {

						SortedSet<Integer> sorter = new TreeSet<Integer>();
						HashSet<?> hS = (HashSet<?>) me.getValue();
						Iterator<?> i2 = hS.iterator();
						while (i2.hasNext()) {
							String strEntry = (String) i2.next();
							paloelement plConsElm = paloElements
									.getElement(strEntry);
							if (plConsElm != null)
								sorter.add((int) plConsElm
										.getElementIdentifier());
						}

						int[] iArrplConsOld = null;
						if (plElm.hasChildren())
							iArrplConsOld = plElm.getElementChildren();

						if (iArrplConsOld != null) {
							for (int oi = 0; oi < iArrplConsOld.length; oi++) {
								sorter.add(iArrplConsOld[oi]);
							}
						}

						Iterator<Integer> itSorted = sorter.iterator();
						StringBuilder sbValues2 = new StringBuilder();
						StringBuilder sbTypes2 = new StringBuilder();

						for (int zi = 0; itSorted.hasNext(); zi++) {
							if (zi > 0) {
								sbValues2.append(",");
								sbTypes2.append(",");
							}
							sbValues2.append(itSorted.next().intValue());
							sbTypes2.append("1");
							// merged[zi] =
							// ((Integer)itSorted.next()).intValue();
						}

						// System.out.println(sbValues2.toString() + " " +
						// sbTypes2.toString());

						// System.out.println("Jetzt");
						// try{
						if (hS != null)
							plElm.updateElementConsolidation(
									sbValues2.toString(), sbTypes2.toString(),
									false);
						// }catch(Exception e){
						// System.out.println("ddd");
						// }

						// paloCons.addElementToConsolidation(paloElements.getElement(tConsElement.getElementName()),tConsElement.getFactor());
						// System.out.println(me.getKey() + " : " +
						// me.getValue() );
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Hier " + e.toString());
		}

	}

}
