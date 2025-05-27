import { NavItem } from './nav-item/nav-item';

export const navItems: NavItem[] = [
  {
    navCap: 'Home',
  },
  {
    displayName: 'Tableau de bord',
    iconName: 'layout-grid-add',
    route: '/dashboard',
  },
  {
    navCap: 'Tables',
  },
  {
    displayName: 'Produits',
    iconName: 'brand-minecraft',
    route: '/ui-components/products',
  },
  {
    displayName: 'RemiseConfig',
    iconName: 'discount',
    route: '/ui-components/discount',
  },
  {
    displayName: 'Remise',
    iconName: 'discount-check',
    route: '/ui-components/linkedProductDiscount',
  },

  {
    displayName: 'Traçage Remise',
    iconName: 'discount',
    route: '/ui-components/trakingTable',
  },
   {
    displayName: 'Traçage Coupon',
    iconName: 'ticket',
    route: '/ui-components/Coupon',
  },



  {
    navCap: 'Auth',
  },
  {
    displayName: 'Login',
    iconName: 'login',
    route: '/authentication',
    children: [
      {
        displayName: 'Login',
        iconName: 'point',
        route: '/authentication/login',
      },
    ],
  },
  
   // {
  //   displayName: 'Register',
  //   iconName: 'user-plus',
  //   route: '/authentication',
  //   children: [
  //     {
  //       displayName: 'Register',
  //       iconName: 'point',
  //       route: '/authentication/register',
  //     },
  //   ],
  // },
];
